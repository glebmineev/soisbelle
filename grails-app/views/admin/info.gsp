<g:applyLayout name="main">
  <tpl:useTemplate template="/adminTemplate">

    <tpl:block name="header">
    </tpl:block>

    <tpl:block name="menu">
      <g:include view="common/adminMenu.gsp"/>
    </tpl:block>

    <tpl:block name="wrap_content">
      <div class="info">
        <h2>Информация о магазине</h2>
        <tpl:zkBody zul="/zul/admin/info.zul"/>
      </div>
    </tpl:block>

    <tpl:block name="footer"/>

  </tpl:useTemplate>
</g:applyLayout>