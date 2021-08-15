function template(input, proto) {
  function ReactiveElement() {
  }
  
  Object.setPrototypeOf(ReactiveElement.prototype, HTMLElement.prototype)
  ReactiveElement.prototype = {...proto, render(locator, model) {
    let root = $("<div>")
    let current = root
    
    function builder(texts, ...params) {
      if (texts.raw) {
        let events = new Map()
        let html = texts.reduce((base, value, index) => {
          let p = params[index - 1]
          if (typeof p == "function") {
            if (base.endsWith("@click=\"")) {
              events.set("click", e => {p(e); this.update || this.update()})
            }
            return base + value
          } else {
            return base + p + value
          }
        }).trim()
        if (html.match(/\/[^\s\u0022\u0027]*>$/)) {
          if (html.startsWith("</")) {
            current = current.parent()
          } else {
            let dom = $(html).appendTo(current)
            events.forEach((value, key) => dom.on(key, value))
          }
        } else {
          current = $(html).appendTo(current)
        }
        
      } else {
        texts.forEach(params[0])
      }
      return builder.bind(this)
    }
    input(builder.bind(this), model)
    $(locator).append(root)
  }}
  
  return ReactiveElement
}

const DocumentNavi = template((h, model) => h
  `<div id="DocNavi" hidden>`(model.docs, doc => h
    `<div class="doc" id="${doc.path}">`
      `<a href="${doc.path}">${doc.title}</a>`
      `<ol class="sub">`(doc.subs, sub => h
        `<li>`
          `<a href="${sub.path}"><svg class="svg" viewBox="0 0 24 24"><use href="/main.svg#chevrons"/></svg>${sub.title}</a>`(sub.subs, foot => h
          `<a href="${foot.path}"><svg class="svg" viewBox="0 0 24 24"><use href="/main.svg#chevrons"/></svg><span class="foot">${foot.title}</span></a>`)
        `</li>`)
      `</ol>`
    `</div>`)
  `</div>`)