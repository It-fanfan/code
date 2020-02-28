package com.fish.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fish.dao.primary.mapper.ManageRoleMapper;
import com.fish.dao.primary.model.ManageRole;
import com.fish.dao.primary.model.ManageUser;
import com.fish.protocols.GetParameter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author CC ccheng0725@outlook.com
 * @date 2020-02-18 20:19
 */
@Service
public class ManageRoleService implements BaseService<ManageRole> {

    @Autowired
    ManageRoleMapper manageRoleMapper;

    @Override
    public void setDefaultSort(GetParameter parameter) { }

    @Override
    public Class<ManageRole> getClassInfo() { return ManageRole.class; }

    @Override
    public boolean removeIf(ManageRole manageRole, JSONObject searchData) {
        return false;
    }

    @Override
    public List<ManageRole> selectAll(GetParameter parameter) {
        JSONArray fileMenuArray = this.getConfigMenuJson();
        Map<String, String> menuNameMap = new HashMap<>();
        for (int i = 0; i < fileMenuArray.size(); i++) {
            JSONObject fileParentMenu = fileMenuArray.getJSONObject(i);
            JSONArray fileChildrenMenu = fileParentMenu.getJSONArray("children");
            for (int j = 0; j < fileChildrenMenu.size(); j++) {
                JSONObject fileChildMenu = fileChildrenMenu.getJSONObject(j);
                menuNameMap.put(fileChildMenu.getString("url"), fileChildMenu.getString("name"));
            }
        }

        List<ManageRole> manageRoleList = this.manageRoleMapper.selectAll();
        for (ManageRole manageRole : manageRoleList) {
            if (manageRole.getId() == 1) {
                manageRole.setPageNames("超级管理员，默认授权全部页面！");
                continue;
            }
            String pageNames = "#";
            if (!StringUtils.isBlank(manageRole.getPages())) {
                for (String page : manageRole.getPages().split("#")) {
                    String pageName = menuNameMap.get(page);
                    if (!StringUtils.isBlank(pageName)) {
                        pageNames += pageName + "#";
                    }
                }
            }
            manageRole.setPageNames(pageNames);
        }
        return manageRoleList;
    }

    public int insert(ManageRole manageRole) {
        if (!StringUtils.isBlank(manageRole.getPages())) {
            manageRole.setPages("#" + manageRole.getPages() + "#");
        }
        return this.manageRoleMapper.insert(manageRole);
    }

    public int update(ManageRole manageRole) {
        if (!StringUtils.isBlank(manageRole.getPages())) {
            manageRole.setPages("#" + manageRole.getPages() + "#");
        }
        return this.manageRoleMapper.update(manageRole);
    }

    public int delete(String deleteIds) { return this.manageRoleMapper.delete(deleteIds); }

    /**
     * 获取某个用户已授权的菜单json，暂时由于权限系统只支持二级菜单<br>
     * JSONObject太多，别看混了，file开头的参数表示从文件中读取的内容
     *
     * @param id
     * @return
     */
    public JSONArray getAuthorizedMenu(String id) {
        JSONArray authorizedMenu = new JSONArray();
        // 1、获取用户及用户权限信息
        Set<String> authorizedPages = null;
        Subject subject = SecurityUtils.getSubject();
        ManageUser manageUser = (ManageUser) subject.getPrincipal();
        if (manageUser == null) {
            return authorizedMenu;
        }
        // 1表示管理员权限
        if (!StringUtils.isBlank(id)) {
            authorizedPages = this.getAuthorizedPageSet(id);
        } else {
            return authorizedMenu;
        }

        // 2、从配置文件读取json配置
        JSONArray fileMenuArray = this.getConfigMenuJson();
        if (fileMenuArray != null) {
            //3、解析从文件中读取的菜单内容
            for (int i = 0; i < fileMenuArray.size(); i++) {
                JSONObject fileParentMenu = fileMenuArray.getJSONObject(i);
                // 3.1、加入父菜单
                JSONObject parentMenu = new JSONObject();
                parentMenu.put("id", i + 1);
                parentMenu.put("parentId", 0);
                parentMenu.put("name", fileParentMenu.getString("name"));
                authorizedMenu.add(parentMenu);
                // 是否有有权限的子菜单
                boolean haveChildrenMenu = false;

                // 3.2、判断子菜单的权限子菜单
                JSONArray fileChildrenMenu = fileParentMenu.getJSONArray("children");
                for (int j = 0; j < fileChildrenMenu.size(); j++) {
                    JSONObject fileChildMenu = fileChildrenMenu.getJSONObject(j);
                    String url = fileChildMenu.getString("url");
                    // 如果是管理员权限或者包含配置的权限
                    if (authorizedPages.contains("*") || authorizedPages.contains(url)) {
                        haveChildrenMenu = true;

                        JSONObject childMenu = new JSONObject();
                        childMenu.put("id", (i + 1) * 100 + j);
                        childMenu.put("parentId", i + 1);
                        childMenu.put("name", fileChildMenu.getString("name"));
                        childMenu.put("url", url + ".html");
                        authorizedMenu.add(childMenu);
                    }
                }

                // 如果一个子菜单都没有，移除上面放入的父菜单
                if (!haveChildrenMenu) {
                    authorizedMenu.remove(parentMenu);
                }
            }
        }
        return authorizedMenu;
    }

    /**
     * 获取配置菜单<br/>
     * 1、剔除【系统设置】下的【用户管理】和【菜单管理】，此页面暂时只默认授予admin账户
     * 2、将菜单处理成符合layui树形组件的模式，参考地址：https://www.layui.com/doc/modules/tree.html<br>
     * 3、JSONObject太多，别看混了，file开头的参数表示从文件中读取的内容
     *
     * @param id 角色ID
     * @return
     */
    public JSONArray getAuthorizationMenu(String id) {
        JSONArray authorizationMenu = new JSONArray();
        // 1、获取用户授权页面字符串
        Set<String> authorizedPages = new HashSet<>();
        if (!StringUtils.isBlank(id) && Integer.parseInt(id) > 0) {
            // 【超级管理员】直接返回
            if ("1".equals(id)){
                return authorizationMenu;
            }
            authorizedPages = this.getAuthorizedPageSet(id);
        }

        // 2、获取菜单文件并处理成符合layui树形组件的模式
        JSONArray fileMenuArray = getConfigMenuJson();
        if (fileMenuArray != null) {
            //3、解析从文件中读取的菜单内容
            for (int i = 0; i < fileMenuArray.size(); i++) {
                JSONObject fileParentMenu = fileMenuArray.getJSONObject(i);
                JSONObject parentMenu = new JSONObject();
                parentMenu.put("title", fileParentMenu.getString("name"));
                // 获取子菜单
                JSONArray fileChildrenMenu = fileParentMenu.getJSONArray("children");
                JSONArray childrenMenu = new JSONArray();
                for (int j = 0; j < fileChildrenMenu.size(); j++) {
                    JSONObject fileChildMenu = fileChildrenMenu.getJSONObject(j);
                    String url = fileChildMenu.getString("url");
                    // 剔除【系统设置】下的【用户管理】和【菜单管理】
                    if (!"manageUser".equals(url) && !"manageRole".equals(url)) {

                        JSONObject childMenu = new JSONObject();
                        // title: '审核配置', id: 'appConfig', checked: true
                        childMenu.put("id", url);
                        childMenu.put("title", fileChildMenu.getString("name"));
                        childMenu.put("checked", authorizedPages.contains(url));
                        // 将数据添加到末尾
                        childrenMenu.add(childMenu);
                    }
                }
                // 如果子菜单有数据，将数据添加到末尾
                if (!childrenMenu.isEmpty()) {
                    parentMenu.put("children", childrenMenu);
                    authorizationMenu.add(parentMenu);
                }
            }
        }
        return authorizationMenu;
    }

    /**
     * 读取系统菜单的配置JSON
     *
     * @return
     */
    public JSONArray getConfigMenuJson() {
        try {
            String fileMenuPath = ManageRoleService.class.getResource("/").getPath() + "menu.json";
            String fileMenuStr = FileUtils.readFileToString(new File(fileMenuPath), "UTF-8");
            JSONArray fileMenuArray = JSONArray.parseArray(fileMenuStr);
            return fileMenuArray;
        } catch (IOException e) {
            System.err.println("ManageRoleService.getConfigMenuJson->读取配置菜单失败！");
            return null;
        }
    }

    /**
     * 获取用户已授权的页面
     *
     * @return
     */
    public Set<String> getAuthorizedPageSet(String roleIds) {
        Set<String> authorizedPageSet = new HashSet<>();
        List<ManageRole> manageRoleList = this.manageRoleMapper.selectRolesByIds(roleIds);
        if (manageRoleList != null) {
            for (ManageRole manageRole : manageRoleList) {
                if (!StringUtils.isBlank(manageRole.getPages())) {
                    String[] pages = manageRole.getPages().split("#");
                    authorizedPageSet.addAll(Arrays.asList(pages));
                }
            }
        }
        return authorizedPageSet;
    }

    /**
     * 将页面提交的JSONObject处理成ManageRole对象
     *
     * @param jsonObject 页面提交的JSONObject
     * @return ManageRole对象
     */
    private ManageRole analysisJson2ManageRole(JSONObject jsonObject) {
        ManageRole manageRole = new ManageRole();
        manageRole.setId(jsonObject.getInteger("id"));
        manageRole.setName(jsonObject.getString("name"));
        // 循环jsonObject，取出pages_开头的参数，处理得到页面配置
        String pages = "";
        Set<String> keySet = jsonObject.keySet();
        for (String key : keySet) {
            if (key.startsWith("pages_") && jsonObject.getBoolean(key)) {
                pages += key.replace("pages_", "#");
            }
        }
        // 前后都用“#”包裹起来，判断是否包含某个页面时使用”#aaaa#“更加精确
        manageRole.setPages(pages + "#");
        return manageRole;
    }

}
