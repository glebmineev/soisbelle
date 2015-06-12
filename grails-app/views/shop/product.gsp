<g:applyLayout name="main">
  <tpl:useTemplate template="/template">

    <tpl:block name="head">
      <title>${title}</title>
      <meta name="Description" content="${description}">
      <meta name="Keywords" content="${keywords}">
      <meta content="index,follow" name="robots">
    </tpl:block>

    <tpl:block name="header">
      <g:include view="common/header.gsp"/>
    </tpl:block>

    <tpl:block name="menu">
      <g:include view="common/menu.gsp"/>
    </tpl:block>

    <tpl:block name="wrap_content">
      <tpl:zkBody zul="/zul/shop/product.zul"/>
    </tpl:block>

    <tpl:block name="footer">
      <g:include view="common/footer.gsp"/>
    </tpl:block>

  </tpl:useTemplate>
</g:applyLayout>