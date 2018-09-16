<#include "common/header.ftl">

<div class="row">

  <div class="col-md-12 mt-1">
  <#if context.flag>
    <div class="float-xs-right">
      <form class="form-inline" action="/action/insert" method="post">
        <div class="form-group">
          <input type="text" class="form-control" id="name" name="name" placeholder="新的用户名称">
        </div>
        <button type="submit" class="btn btn-primary">新建</button>
      </form>
    </div>
  </#if>
    <h1 class="display-4">${context.title}</h1>
    <div class="float-xs-right">
      <a class="btn btn-outline-danger" href="/logout" role="button" aria-pressed="true">退出(${context.username}
        )</a>
    </div>
  </div>

  <div class="col-md-12 mt-1">
  <#list context.list>
    <h2>Pages:</h2>
    <ul>
      <#items as user>
        <li><a href="/wiki/${page}">${page}</a></li>
      </#items>
    </ul>
  <#else>
    <p>没有查询到用户</p>
  </#list>

  <#if context.flag>
    <#if context.backup_gist_url?has_content>
      <div class="alert alert-success" role="alert">
        Successfully created a backup:
        <a href="${context.backup_gist_url}" class="alert-link">${context.backup_gist_url}</a>
      </div>
    <#else>
      <p>
        <a class="btn btn-outline-secondary btn-sm" href="/action/backup" role="button" aria-pressed="true">Backup</a>
      </p>
    </#if>
  </#if>
  </div>

</div>

<#include "common/footer.ftl">
