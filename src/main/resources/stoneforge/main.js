// =====================================================
// The imitation of jQuery
// =====================================================
$ = Mimic = (query, ...args) => {
  if (!Mimic.html) {
    let noop = () => {}
  
    Mimic.prototype = {
      each: self((e, action) => action(e)),
      
      contain: flat((e, selector) => e.querySelector(selector) ? [e] : [], 9),
      filter: flat((e, condition) => condition(e) ? [e] : [], 9),
      is: flat((e, selector) => e.matches(selector), 9),
      
      parent: flat(e => [e.parentNode]),
      parents: flat(e => all(e, x => x.parentElement)),
      closest: self((e, selector) => e.closest(selector)),
      children: flat(e => e.children),
      find: flat((e, selector) => e.querySelectorAll(selector), 9),
      first: flat(e => [e.firstElementChild]),
      last: flat(e => [e.lastElementChild]),
      prev: flat(e => [e.previousElementSibling]),
      prevs: flat(e => all(e, x => x.previousElementSibling)),
      prevUntil: flat((e, selectorOrElement) => all(e, x => x.previousElementSibling, selectorOrElement), 1),
      next: flat(e => [e.nextElementSibling]),
      nexts: flat(e => all(e, x => x.nextElementSibling)),
      nextUntil: flat((e, selectorOrElement) => all(e, x => x.nextElementSibling, selectorOrElement), 1),
      
      append: self((e, node) => nody(node, n => e.append(n))),
      appendTo: self((e, node) => nody(node, n => n.append(e))),
      prepend: self((e, node) => nody(node, n => e.prepend(n))),
      prependTo: self((e, node) => nody(node, n => n.prepend(e))),
      before: self((e, node) => nody(node, n => e.before(n))),
      insertBefore: self((e, node) => nody(node, n => n.before(e))),
      after: self((e, node) => nody(node, n => e.after(n))),
      insertAfter: self((e, node) => nody(node, n => n.after(e))),
      clone: self(e => e.cloneNode()),
      make: flat((e, nameOrItems, action) => Mimic.isString(nameOrItems) ? [e.appendChild(document.createElement(nameOrItems))] : nameOrItems.map(action).flatMap(e => e.nodes), 9),
      svg: shorthand((e, path) => e.append("<svg class='svg' viewBox='0 0 24 24'><use href='"+ path +"'/></svg>").last()),
      
      empty: self(e => e.replaceChildren()),
      clear: self(e => e.parentNode.removeChild(e)),
      
      html: value((e, text) => text ? e.innerHTML = text : e.innerHTML), 
      text: value((e, text) => text ? e.textContent = text : e.textContent),
      attr: value((e, name, value) => value ? e.setAttribute(name, value) : e.getAttribute(name)),
      data: value((e, name, value) => value ? e.dataset[name] = value : e.dataset[name]),
      css : self((e, style) => Mimic.isString(style) ? e.style.cssText = style : Object.keys(style).forEach(name => e.style[name] = style[name])),
      
      add: value((e, name) => e.classList.add(name)),
      remove: value((e, name) => e.classList.remove(name)),
      toggle: value((e, name, addAction=noop, removeAction=noop) => e.classList.toggle(name) ? addAction() : removeAction()),
      has: value((e, name) => e.classList.contains(name)),
      set: value((e, nameAndCondition) => Object.keys(nameAndCondition).forEach(name => e.classList[nameAndCondition[name] ? "add" : "remove"](name))),
      reset: value((e, name) => e.className = (name || "")),
      
      // In cases where event listeners are registered during event processing, we delay the registration of all event listeners
      // because it may cause an infinite loop or supplement the event at an unintended timing.
      on: value((e, type, listener, options) => {setTimeout(() => e.addEventListener(type, listener, options), 0)}),
      off: value((e, type, listener) => e.removeEventListener(type, listener)),
    }
    
    "blur focus focusin focusout resize scroll click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup contextmenu".split(" ").forEach(type => {
      Mimic.prototype[type] = function(listener, options) { return this.on(type, listener, options) }
    })
    
    function* all(e, action, stopper) {
      let stop = stopper ? Mimic.isString(stopper) ? e => e.matches(stopper) : e => e === stopper : e => false
      while((e = action(e)) && !stop(e)) yield e
    }
    
    function flat(traverser, filterIndex = 0) {
      return function(...arg) {
        let nodes = [...new Set(this.nodes.flatMap(n => [...traverser(n, ...arg)]))]
        return Mimic(!arg[filterIndex] ? nodes : nodes.filter(e => e.matches(arg[filterIndex])))
      }
    }
    
    function self(action) {
      return function(...arg) {
        return Mimic(this.nodes.map(n => action(n, ...arg) || n))
      }
    }
    
    function value(action) {
      return function(...arg) {
        let result = this.nodes.map(n => action(n, ...arg))[0]
        return result === undefined || result === arg[arg.length - 1] ? this : result
      }
    }
    
    function shorthand(action) {
      return function(...arg) {
        return action(this, ...arg)
      }
    }
    
    function nody(v, action) {
      if (v instanceof Element || v instanceof Text || v instanceof ShadowRoot) {
        action(v)
      } else if (Array.isArray(v)) {
        v.forEach(i => nody(i, action))
      } else if (Mimic.isString(v)) {
        action(v.trim()[0] === "<" ? Mimic.html(v) : $(v))
      } else if (v instanceof Mimic) {
        nody(v.nodes, action)
      }
    }
    
    Mimic.isString = v => typeof v === "string" || v instanceof String
    Mimic.html = text => {
      let t = document.createElement("template")
      t.innerHTML = text
      return t.content
    }
  }
  
  let o = Object.create(Mimic.prototype)
  o.nodes = Mimic.isString(query) ? [...(query.trim()[0] === "<" ? Mimic.html(query).children : document.querySelectorAll(query))]
          : Array.isArray(query) ? query
          : !query ? [document]
          : query instanceof Node ? [query]
          : query instanceof Mimic ? [...query.nodes]
          : /* query instanceof NodeList || query instanceof HTMLCollection ? */ [...query]
  return o
}

// =====================================================
// User Settings
// =====================================================
const
user = JSON.parse(localStorage.getItem("user")) || {},
save = () => localStorage.setItem("user", JSON.stringify(user)),

// =====================================================
// Utilities
// =====================================================
html = $("html"),
svg = (type) => {
  var a = document.createElement("a");
  a.innerHTML = `<svg class="svg" viewBox="0 0 24 24"><use href="/main.svg#${type}"/></svg>`;
  return a;
}


// =====================================================
// View Mode
// =====================================================
html.add(user.theme)
$("#light,#dark").on("click", e => save(html.reset(user.theme = e.currentTarget.id)))


// =====================================================
// Dynamic Navigation Indicator
// =====================================================
const navi = new IntersectionObserver(e => {
  e = e.filter(i => i.isIntersecting);
  if (0 < e.length) {
    const i = e.reduce((a,b) => a.intersectionRation > b.intersectionRatio ? a : b);
    if (i) {
      console.log(i.target.id, i.intersectionRatio);
      $("#DocNavi .now").remove("now");
      $(`#DocNavi a[href$='#${i.target.id}']`).add("now");
    }
  }
}, {root: null, rootMargin: "-40% 0px -60% 0px", threshold: 0})

// =====================================================
// Lightning Fast Viewer
// =====================================================
function FlashMan({paged, cacheSize=20, preload="mouseover", preview="section", ...previews}) { 
  var path = location.pathname, hash = location.hash;
  const cache = new Map(), loading = new Set(), observer = new IntersectionObserver(set => {
    set.filter(x => x.isIntersecting && !x.target.init && (x.target.init = true)).forEach(x => {
      for (let q in previews) x.target.querySelectorAll(q).forEach(e => previews[q](e))
    })
  }, {rootMargin: "80px", threshold: 0.3});
  
  // This is the state immediately after a page change has been requested by a user operation.
  function changed() {
    if (path == location.pathname) {
      if (hash != location.hash) {
        hash = location.hash;
        hashed();
      }
    } else {
      path = location.pathname;
      hash = location.hash;
      load(path);
    }
  }
  
  // Reads the contents of the specified path into the cache. If it is already cached or currently being read, it will be ignored.
  function load(p) {
    if (cache.has(p)) {
      if (path == p) update(cache.get(p))
    } else if (!loading.has(p)) {
      loading.add(p)
      fetch(p)
        .then(response =>  response.text())
        .then(html => {
          loading.delete(p)
          cache.set(p, html)
          if (path == p) update(html)
          if (cacheSize < cache.size) cache.delete(cache.keys().next().value)
        })
    }
  }
  
  function update(text) {
    if (text) {
      $("article").html(text.substring(text.indexOf(">", text.indexOf("<article")) + 1, text.lastIndexOf("</article>")));
      $("aside").html(text.substring(text.indexOf(">", text.indexOf("<aside")) + 1, text.lastIndexOf("</aside>")));
    }
    paged();
    $(preview).each(e => observer.observe(e));
    hashed();
  }
  
  function hashed() {
    // scroll to top or #hash
    if (location.hash == "") {
      setTimeout(() => window.scrollTo(0,0), 200); // wait rendering
    } else {
      location.replace(location.hash);
    }
  }

  // Detect all URL changes
  window.addEventListener("popstate", v => changed())
  document.addEventListener("DOMContentLoaded", v => {update(); cache.set(location.pathname, document.documentElement.outerHTML)})
  document.addEventListener("click", v => {
    let e = v.target.closest("a");
    if (e != null && location.origin == e.origin) {
      if (location.href != e.href) {
        history.pushState(null, null, e.href)
        changed()
      }
      v.preventDefault()
    }
  })
  // Preloader
  document.addEventListener(preload, v => {
    let e = v.target, key = e.pathname;
    if (e.tagName === "A" && e.origin == location.origin && key != location.pathname && !cache.has(key)) load(key)
  })
}

FlashMan({
  paged: () => {
    $("#APINavi").each(e => e.hidden = !location.pathname.startsWith("/api/"));
    $("#DocNavi").each(e => e.hidden = !location.pathname.startsWith("/doc/"));
    $("#DocNavi>div").each(e => {
      const sub = e.lastElementChild;
      
      if (e.id == location.pathname) {
        e.classList.add("active");
        sub.style.height = sub.scrollHeight + "px";
      } else {
        e.classList.remove("active");
        sub.style.height = 0;
      }
    });
    
    $("#Article section").each(e => navi.observe(e));
  },
  
  preview: "#Article>section",
  /* Enahnce code highlight */
  "pre": e => { 
    hljs.highlightElement(e);
    e.lang = e.classList[0].substring(5).toUpperCase();
    var a = svg("copy");
    a.title = "Copy this code";
    a.onclick = () => navigator.clipboard.writeText(e.textContent);
    e.appendChild(a);
  },
  /* Enahnce meta icons */
  ".perp": e => {
    e.title = "Copy the permanent link";
    e.onclick = () => navigator.clipboard.writeText(location.origin + location.pathname + "#" + e.closest("section").id);
  }, ".tweet": e => {
    e.title = "Post this article to Twitter";
    e.href = "https://twitter.com/intent/tweet?url=" + encodeURIComponent(location.origin + location.pathname + "#" + e.closest("section").id) + "&text=" + encodeURIComponent(e.closest("header").firstElementChild.textContent);
    e.target = "_blank";
    e.rel = "noopener noreferrer";
  }, ".edit": e => {
    e.title = "Edit this article";
    e.target = "_blank";
    e.rel = "noopener noreferrer";
}});

// =====================================================
// Define Components
// =====================================================
new Vue({
  el: "main"
});

new Vue({
  el: "nav > div",
  template: `
	<div>
		<div id="DocNavi" hidden>
      <div class="doc" v-for="doc in items.docs" :id="doc.path">
        <a :href="doc.path">{{doc.title}}</a>
        <ol class="sub">
          <li v-for="sub in doc.subs">
            <a :href="sub.path"><svg class="svg" viewBox="0 0 24 24"><use href="/main.svg#chevrons"/></svg>{{sub.title}}</a>
            <a v-for="foot in sub.subs" :href="foot.path"><svg class="svg" viewBox="0 0 24 24"><use href="/main.svg#chevrons"/></svg><span class="foot">{{foot.title}}</span></a>
          </li>
        </ol>
      </div>
    </div>
	  <div id="APINavi" hidden>
	    <xxx/>
  		<o-select placeholder="Select Module" model:="root.modules"/>
  		<o-select placeholder="Select Package" model:="root.packages"/>
  		<o-select placeholder="Select Type" separator=", " model:="['Interface','Functional','AbstractClass','Class','Enum','Annotation','Exception']"/>

  		<input id="SearchByName" v-model="selectedName" placeholder="Search by Name">
  		
  		<div class="tree">
  			<dl v-for="package in sortedItems">
  				<dt @click="toggle(package)" v-show="filter(package.children).length"><code>{{package.name}}</code></dt>
  				<dd v-for="type in filter(package.children)" :class="type.type" v-show="expandAll || package.isOpen"><code><a :href="'/api/'+type.packageName+'.'+type.name+'.html'">{{type.name}}</a></code></dd>
  			</dl>
  		</div>
    </div>
	</div>
  `,
  data: function() {
    root.docs.forEach(e => e.isOpen = false);
    console.log(root);
  
    return {
      items: root,
      sortedItems: this.sortAndGroup(root),
      selectedName: "",
      selectedPackage: null,
      selectedModule: null,
      selectedType: [],
      expandAll: false
    };
  },

  methods: {
    sortAndGroup: function(items) {
      let map = new Map();
      items.packages.forEach(item => {
        map.set(item, {
          name: item,
          children: [],
          isOpen: false
        });
      });

      items.types.forEach(item => {
        map.get(item.packageName).children.push(item);
      });

      return Array.from(map.values());
    },
    filter: function(items) {
      this.expandAll = this.selectedType.length !== 0 || this.selectedPackage !== null || this.selectedName !== "";

      return items.filter(item => {
        if (this.selectedType.length != 0 && !this.selectedType.includes(item.type)) {
          return false;
        }

        if (this.selectedPackage !== null && this.selectedPackage !== item.packageName) {
          return false;
        }

        if (this.selectedName !== "" && (item.packageName + "." + item.name).toLowerCase().indexOf(this.selectedName.toLowerCase()) === -1) {
          return false;
        }
        return true;
      });
    },
    toggle: function(package) {
      package.isOpen = !package.isOpen;
    }
  }
});

// =====================================================
// Live Reload
// =====================================================
if (location.hostname == "localhost") setInterval(() => fetch("http://localhost:9321/live").then(res => {
  if(res.status == 200) location.reload();
}), 3000);




class Base extends HTMLElement {

  constructor() {
    super()
    this.attachShadow({mode: "open"})
    this.root = $(this.shadowRoot).append($("head link[rel=stylesheet]").clone())
    
    Array.from(this.attributes).forEach(v => {
      let name = v.name, value = v.value
      switch (name.charAt(name.length - 1)) {
        case ":":
          name = name.substring(0, name.length - 1)
          value = Function("return " + value)()
          break;
      }
      this[name] = value
    })
  }
}

customElements.define("o-select", class Select extends Base {
  
  view    = this.root.make("view").click(() => this.list.has("active") ? this.close() : this.open())
    now   = this.view.make("now").text(this.placeholder)
    del   = this.view.svg("/main.svg#x").click(e => {e.stopPropagation(); this.deselect()})
    mark  = this.view.svg("/main.svg#chevron")
  list    = this.root.make("ol")
    items = this.list.make(this.model, item => this.list.make("li").text(this.render(item)).click(e => this.select(item, $(e.target))))
    
  selected = new Set()
  
  constructor() {
    super()
    
    $(this).set({disabled: !this.model.length})
    this.closer = e => {
      if (!this.contains(e.target)) this.close()
    }
  }
  
  render(item) {
    return item.toString()
  }
  
  select(item, dom) {
    if (this.separator) {
      dom.toggle("select", () => this.selected.add(item), () => this.selected.delete(item)) 
    } else {
      this.deselect()
      dom.add("select")
      this.selected.add(item)
    }
    
    this.update()
  }
  
  deselect() {
    this.items.remove("select")
    this.selected.clear()
    this.close()
    
    this.update()
  }
  
  update() {
    this.now.set({select: this.selected.size}).text([...this.selected.keys()].map(this.render).join(this.separator) || this.placeholder)
    this.del.set({active: this.selected.size})
  }
  
  open() {
    this.list.add("active")
    this.mark.add("active")
    $(document).click(this.closer)
  }
  
  close() {
    this.list.remove("active");
    this.mark.remove("active");
    $(document).off("click",this.closer)
  }
})


var count = 0 // 変数を追加
var Hello = {
    view() {
        return m("main", [
            m("h1", {class: "title"}, "最初のMithrilアプリケーション"),
            // この行を変更
            m("button", {onclick: function() {count++}}, count + " クリック"),
        ])
    }
}

function Select({placeholder="Select Item", model=[], separator, render=v=>v.toString()}) {
  const selected = this.selected = new Set()

  function select(item) {
    if (separator) {
      //dom.toggle("select", () => this.selected.add(item), () => this.selected.delete(item)) 
    } else {
      deselect()
      //dom.add("select")
      selected.add(item)
    }
  }
  
  function deselect() {
    //this.items.remove("select")
    selected.clear()
   // this.close()
    
   // this.update()
  }
  
  this.view = () => [
    m("view", [
      m("now", [...selected.keys()].map(render).join(separator) || placeholder)
    ]),
    m("ol", model.map(item => m("li", {onclick: e=>select(item)}, render(item))))
  ]
  return this;
}

const PackageFilter = new Select({placeholder:"Select Package", model: root.packages})

m.mount(document.querySelector("xxx"), PackageFilter)