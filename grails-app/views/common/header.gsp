<%@ page import="ru.spb.soisbelle.importer.ConvertUtils" %>
<%
  def loginService = grailsApplication.mainContext.getBean("loginService");
  def cartService = grailsApplication.mainContext.getBean("cartService");
%>
<table width="100%" cellpadding="0" cellspacing="5" style="margin-top: 10px;">
  <tr align="left">
    <td width="50%" valign="top">
      <tpl:zkBody zul="/zul/search/search.zul"/>
    </td>
    <td width="25%" valign="top" align="right">
      <div class="menu-element" style="width: 211px;">
        <div>
          <g:link controller="shop" action="register">Регистрация</g:link>
        </div>
      </div>
    </td>
    <td width="20%" valign="top" align="right">
      <div class="menu-element" style="width: 118px;margin-right: 10px;">
        <div>
          <g:if test="${!loginService.isLogged()}">
            <g:link controller="auth" action="login">Войти</g:link>
          </g:if>
          <g:else>
            <g:link controller="auth" action="logout">Выйти</g:link>
          </g:else>
        </div>
      </div>
    </td>
    <td width="10%" valign="top" align="right">
      <div class="cart">
        <div class="cart-img">
          <g:link controller="shop" action="cart">
            <g:img dir="images/dsn/header" file="cart32.png"/>
          </g:link>
        </div>

        <div class="cart-counters">
          <span id="totalCount">${cartService.getTotalCount()} товар</span>
          <br/>
          <span id="totalPrice">${cartService.getTotalPrice()} Р</span>
        </div>
      </div>

    </td>
  </tr>
  <tr align="left" valign="top">
    <td colspan="4">
      <g:link controller="shop" action="index">
        <g:img dir="images/dsn" file="sois_bellex50.png"/>
      </g:link>
    </td>
  </tr>
</table>
%{--<table width="100%" cellpadding="0" cellspacing="0">
  <tr align="left">
    <g:if test="${loginService.isLogged()}">
      <td width="32px">
        <g:img dir="images" file="truck.png"/>
      </td>
      <td width="110px">
        <g:link controller="cabinet" action="index">
          Личный кабинет
        </g:link>
      </td>
      <td>
        <g:link controller="auth" action="logout">
          Выйти
        </g:link>
      </td>
    </g:if>
    <g:else>
      <td width="32px">
        <g:img dir="images" file="unlock.png"/>
      </td>
      <td width="110px">
        <g:link controller="shop" action="register">
          Регистрация
        </g:link>
      </td>
      <td>
        <g:link controller="auth" action="login">
          Войти
        </g:link>
      </td>
    </g:else>
  </tr>
</table>
<table width="100%" cellpadding="0" cellspacing="0">
  <tr>
    <td colspan="2" width="10%" align="left">
      <g:link controller="shop" action="index">
        <g:img dir="images" file="logo.png"/>
      </g:link>
    </td>
    <td>
      <table width="100%">
        <tr>
          <td colspan="4">
            <table width="100%">
              <tr>
                <td align="right">
                  <g:img dir="images" file="phone.png"/>
                </td>
                <td align="left">
                  8 (812) 448 77 55
                </td>
              </tr>
            </table>
          </td>
          <td colspan="2">
            <table width="100%">
              <tr>
                <td align="right">
                  <g:img dir="images" file="clock.png"/>
                </td>
                <td align="left">
                  мы работаем с 9 до 21
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td>
            <table width="100%">
              <tr>
                <td align="right">
                  <g:img dir="images" file="contacts.png"/>
                </td>
                <td align="left">
                  <g:link controller="shop" action="contacts" params="[type: 'contacts']">Контакты</g:link>
                </td>
              </tr>
            </table>
          </td>
          <td>
            <table width="100%">
              <tr>
                <td align="right">
                  <g:img dir="images" file="about.png"/>
                </td>
                <td align="left">
                  <g:link controller="shop" action="about" params="[type: 'about']">О нас</g:link>
                </td>
              </tr>
            </table>
          </td>
          <td>
            <table width="100%">
              <tr>
                <td align="right">
                  <g:img dir="images" file="transport.png"/>
                </td>
                <td align="left">
                  <g:link controller="shop" action="delivery" params="[type: 'transport']"
                          style="font-size: 10pt">О доставке</g:link>
                </td>
              </tr>
            </table>
          </td>
          <td>
            <table width="100%">
              <tr>
                <td align="right">
                  <g:img dir="images" file="checkout32.png"/>
                </td>
                <td align="left">
                  <g:link controller="shop" action="about" params="[type: 'transport']">Как покупать</g:link>
                </td>
              </tr>
            </table>
          </td>
          <td colspan="2">
            <tpl:zkBody zul="/zul/search/search.zul"/>
            --}%%{--<g:form controller="search" action="index">
              <g:textField name="query" size="16"/>
              <g:submitButton name="Найти">
                <g:img dir="images" file="search.png"/>
              </g:submitButton>
            </g:form>--}%%{--
          </td>
        </tr>
        <tr>
          <td colspan="6" align="right">
            <table width="50%">
              <tr>
                <td width="100px" align="right">
                  <table>
                    <tr>
                      <td>
                        товаров:
                      </td>
                      <td>
                        <div
                            id="totalCount">${cartService.getTotalCount()}</div>
                      </td>
                    </tr>
                  </table>
                </td>
                <td width="100px" align="right">
                  <table>
                    <tr>
                      <td>
                        <div id="totalPrice">${ConvertUtils.roundFloat((Float) cartService.getTotalPrice())}</div>

                        <div id="sessionCart"></div>
                      </td>
                      <td>
                        руб.
                      </td>
                    </tr>
                  </table>
                </td>
                <td width="48px" align="right">
                  <g:link controller="shop" action="cart">
                    <g:img dir="images" file="cart_red.png"/>
                  </g:link>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>--}%