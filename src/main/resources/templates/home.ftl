<#include "common/header.ftl">

<div class="row">

  <div class="col-md-12 mt-1">
  <#if context.flag>
    <div class="float-xs-right">
      <form class="form-inline" action="/user/insert" method="post">
        <div class="form-group">
          <label>姓名</label>
          <input type="text" name="username">
          <br>
          <label>密码</label>
          <input type="password" name="password">
          <br>
          <label>手机</label>
          <input type="text" name="mobile">
          <br>
          <label>性别</label>
          <input type="checkbox" name="sex" value="male">男
          <input type="checkbox" name="sex" value="female">女
          <br>
          <label>选择国家</label>
          <select name="address">
            <option value="US">美国</option>
            <option value="FR">法国</option>
            <option value="UK">英国</option>
            <option value="CN">中国</option>
          </select>
        </div>
        <button type="submit" class="btn btn-primary">添加用户</button>
      </form>
    </div>
  </#if>
  </div>

  <div class="col-md-12 mt-1">
  <#list context.list>
    <h2>用户详情：</h2>
    <#items as user>
    <label>姓名</label>
          <input type="text" name="username" value="${user.username}">
          <br>
          <label>密码</label>
          <input type="password" name="password" value="${user.password}">
          <br>
          <label>手机</label>
          <input type="text" name="mobile" value="${user.mobile}">
          <br>
          <label>性别</label>
          <input type="checkbox" name="sex" <#if user.sex == "male"> checked </#if> value="male">男
          <input type="checkbox" name="sex" <#if user.sex == "female"> checked </#if> value="female">女
          <br>
          <label>国家</label>
          <select name="address">
            <option <#if user.address == "US"> selected </#if> value="US">美国</option>
            <option <#if user.address == "FR"> selected </#if> value="FR">法国</option>
            <option <#if user.address == "UK"> selected </#if> value="=UK">英国</option>
            <option <#if user.address == "CN"> selected </#if> value="CN">中国</option>
          </select>
    </#items>
  </#list>
  </div>

</div>

<#include "common/footer.ftl">
