; // <script src="//npmcdn.com/show-current-browser-url"></script>

(function() {
  var div = document.createElement("div")
  div.id = "document_location";
  div.style.position = "fixed";
  div.style.top = "0";
  div.style.left = "0";
  div.style.right = "0";
  div.style.height = "20px;";
  div.style.display = "flex";
  div.style.padding = "3px";
  div.style['font-family'] = "helvetica";
  div.style['font-size'] = "0.9em";
  div.style['align-items'] = "baseline";
  div.style["border-width"] = "1px 0"
  div.style["border-style"] = "solid"
  div.style["border-color"] = "lightgrey"
  
  var label = document.createElement('span');
  label.innerHTML = "url: "
  label.style.padding = "0 0.25em 0 0 ";
  div.appendChild(label);
  
  var loc = document.createElement('code');
  loc.style.flex = "0 1 auto"
  loc.style.overflow = "hidden"
  loc.style.whiteSpace = "nowrap"
  div.appendChild(loc);
  
  function init() {
    var body =  window.document.getElementsByTagName('body')[0]
    body.appendChild(div);
    body.style["padding-top"] = "20px";
    update();
  }
  
  function trim(el) {
    if (el.scrollWidth > el.offsetWidth) {
      var textNode = el.firstChild;
      var value = '...' + textNode.nodeValue;
      do {
        value = '...' + value.substr(4);
        textNode.nodeValue = value;
      } while (value.length > 4 && el.scrollWidth > el.offsetWidth);
    }
  }
  
  var previousVals = {
    location: null,
    scrollWidth: null,
    offsetWidth: null,
    windowWidth: null
  }
  
  function update() {
    if (document.location == previousVals.location && 
        loc.scrollWidth == previousVals.scrollWidth &&
        loc.offsetWidth == previousVals.offsetWidth &&
        window.innerWidth == previousVals.windowWidth) {
      return;
    }

    previousVals.location = document.location.href;
    previousVals.scrollWidth = loc.scrollWidth;
    previousVals.offsetWidth = loc.offsetWidth;
    previousVals.windowWidth = window.innerWidth;

    loc.innerHTML = document.location.href;
    trim(loc);
  }
  
  window.addEventListener("hashchange", update, false);
  window.addEventListener("popstate", update, false);
  window.addEventListener("resize", update, false);
  document.addEventListener('DOMContentLoaded', init, false);
  setInterval(update, 75);
})();
