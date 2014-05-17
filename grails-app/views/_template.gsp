<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>

  <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>

  <g:javascript library="application"/>
  %{--<g:javascript library="jquery" src="jquery-1.7.1.min.js"/>--}%
  <g:javascript library="nivo" src="slider/jquery.nivo.slider.js"/>
  %{--<g:javascript library="carousel" src="carousel/jquery.jcarousel.min.js"/>
  <g:javascript library="ckeditor" src="ckeditor.js"/>
  <g:javascript library="megamenu" src="megamenu/jquery.dcmegamenu.1.3.3.js"/>
  <g:javascript library="hoverIntent" src="megamenu/jquery.hoverIntent.minified.js"/>--}%

  <link rel="stylesheet" href="${resource(dir: 'css', file: 'content.css')}" type="text/css">

  <!-- slider stylesheet -->
%{--  <link rel="stylesheet" href="${resource(dir: 'css/slider/themes/default', file: 'default.css')}" type="text/css"
        media="screen"/>
  <link rel="stylesheet" href="${resource(dir: 'css/slider/themes/light', file: 'light.css')}" type="text/css"
        media="screen"/>
  <link rel="stylesheet" href="${resource(dir: 'css/slider/themes/dark', file: 'dark.css')}" type="text/css"
        media="screen"/>
  <link rel="stylesheet" href="${resource(dir: 'css/slider/themes/bar', file: 'bar.css')}" type="text/css"
        media="screen"/>--}%
  <link rel="stylesheet" href="${resource(dir: 'css/slider', file: 'nivo-slider.css')}"/>

  <!-- drop menu -->
  %{--<link rel="stylesheet" href="${resource(dir: 'css/dropmenu', file: 'dropmenu.css')}"/>--}%

  <!-- Handle menu -->
  %{--<link rel="stylesheet" href="${resource(dir: 'css/dropmenu', file: 'handleMenu.css')}"/>--}%

  <!-- Megamenu -->
  %{--<link rel="stylesheet" href="${resource(dir: 'css/megamenu', file: 'megamenu.css')}"/>
  <link rel="stylesheet" href="${resource(dir: 'css/megamenu/skins', file: 'green.css')}"/>
  <link rel="stylesheet" href="${resource(dir: 'css/megamenu/skins', file: 'black.css')}"/>--}%

  <!-- Carousel -->
  %{--<link rel="stylesheet" href="${resource(dir: 'css/carousel', file: 'carousel.css')}"/>--}%

  <!-- ZK -->
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'ZK.css')}"/>
  ${head}

  <!-- вертикальный скроллинг -->
  <script type="text/javascript">
    window.onscroll = updateScrollButtons;
    window.onload = updateScrollButtons;

    function updateScrollButtons() {
      document.getElementsByClassName('scrollUp')[0].style.display = window.pageYOffset == 0 ? 'none' : 'block'
      document.getElementsByClassName('scrollDown')[0].style.display = window.pageYOffset + window.innerHeight == Math.max(
          Math.max(document.body.scrollHeight, document.documentElement.scrollHeight),
          Math.max(document.body.offsetHeight, document.documentElement.offsetHeight),
          Math.max(document.body.clientHeight, document.documentElement.clientHeight)) ? 'none' : 'block'
      document.getElementsByClassName('scrollDown')[0].style.top = (window.innerHeight - 90) + 'px';
    }
  </script>
  <r:layoutResources/>
</head>

<body>

<div class="center">

  <div id="header">
    ${header}
  </div>

  <div id="menu">
    ${menu}
  </div>

  <div id="wrap_content">
    ${wrap_content}
  </div>

  <div id="footer">
    ${footer}
  </div>

</div>

  <!-- вертикальный скроллинг -->
  <div class="scrollUp" onclick="window.scrollTo(0, 0); " align="center">
    <g:img dir="images" file="moveUp.png" />
  </div>

  <div class="scrollDown" onclick="window.scrollTo(0, Math.max(
      Math.max(document.body.scrollHeight, document.documentElement.scrollHeight),
      Math.max(document.body.offsetHeight, document.documentElement.offsetHeight),
      Math.max(document.body.clientHeight, document.documentElement.clientHeight))); " align="center">
    <g:img dir="images" file="moveDown.png"/>
  </div>

</body>
</html>