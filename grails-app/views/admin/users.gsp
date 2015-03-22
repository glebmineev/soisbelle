<g:applyLayout name="main">
  <tpl:useTemplate template="/adminTemplate">

    <tpl:block name="header">
    </tpl:block>

    <tpl:block name="menu">
      <g:include view="common/adminMenu.gsp"/>
    </tpl:block>

    <tpl:block name="wrap_content">
      <tpl:zkBody zul="/zul/admin/users.zul"/>
    </tpl:block>

    <tpl:block name="footer"/>

  </tpl:useTemplate>
</g:applyLayout>