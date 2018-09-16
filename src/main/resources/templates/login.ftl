<#include "common/header.ftl">

<div class="row">

  <div class="col-md-12 mt-1">
    <form action="/loginAction" method="POST">
      <div class="form-group">
        <table>
          <tr>
            <td>
              用户名：<input type="text" name="username" placeholder="用户名">
            </td>
          </tr>
          <tr>
            <td>
              密&nbsp;&nbsp;码：<input type="password" name="password" placeholder="密码">
            </td>
          </tr>
          <tr>
            <td>
              <input type="hidden" name="return_url" value="/index">
            </td>
          </tr>
        </table>
        <button type="submit" class="btn btn-primary">登录</button>
      </div>
    </form>
  </div>

</div>

<#include "common/footer.ftl">
