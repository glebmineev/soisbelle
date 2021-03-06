<g:applyLayout name="main">
  <tpl:useTemplate template="/template">

    <tpl:block name="header">
      <g:include view="common/header.gsp"/>
    </tpl:block>

    <tpl:block name="menu">
      <g:include view="common/menu.gsp"/>
    </tpl:block>

    <tpl:block name="wrap_content">
      <div class="info">
        Благодарим Вас за обращение к интернет-магазину «Sois belle»
        <br/>
        Ваш заказ успешно обработан.
        <br/>
        Вам направленно письмо с номером заказана Ваш электронный адрес
      </div>
    </tpl:block>

    <tpl:block name="footer">
      <g:include view="common/footer.gsp"/>
    </tpl:block>

  </tpl:useTemplate>
</g:applyLayout>