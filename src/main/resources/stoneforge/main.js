// =====================================================
// The imitation of jQuery
// =====================================================
$ = Mimic = (query, ...args) => {
  if (!Mimic.html) {
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
      make: flat((e, nameOrItems, action) => Mimic.isString(nameOrItems) ? [e.appendChild(document.createElement(nameOrItems))] : []),
      
      empty: self(e => e.replaceChildren()),
      clear: self(e => e.parentNode.removeChild(e)),
      
      html: value((e, text) => text ? e.innerHTML = text : e.innerHTML), 
      text: value((e, text) => text ? e.textContent = text : e.textContent),
      attr: value((e, name, value) => value ? e.setAttribute(name, value) : e.getAttribute(name)),
      data: value((e, name, value) => value ? e.dataset[name] = value : e.dataset[name]),
      toString: value(e => e.outerHTML),
      
      add: value((e, name) => e.classList.add(name)),
      remove: value((e, name) => e.classList.remove(name)),
      toggle: value((e, name) => e.classList.toggle(name)),
      has: value((e, name) => e.classList.contains(name)),
      set: value((e, name) => e.className = name || ""),
      
      on: value((e, type, handler) => e.addEventListener(type, handler)),
      off: value((e, type) => e.removeEventListener(type))
    }
    
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
  
  if (Array.isArray(query) && Array.isArray(query.raw)) {
    query = query.raw.reduce((acc, lit, i) => {
      let placeholder = args[i - 1]
      if (Array.isArray(placeholder)) placeholder = placeholder.join("")
      return acc + placeholder + lit
    })
  }
  
  let o = Object.create(Mimic.prototype)
  o.nodes = Mimic.isString(query) ? [...(query.trim()[0] === "<" ? Mimic.html(query).children : document.querySelectorAll(query))]
          : Array.isArray(query) ? Array.isArray(query.raw) ? literal(query) : query
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
nop = () => {},
svg = (type) => {
  var a = document.createElement("a");
  a.innerHTML = `<svg class="svg" viewBox="0 0 24 24"><use href="/main.svg#${type}"/></svg>`;
  return a;
}


// =====================================================
// View Mode
// =====================================================
html.add(user.theme)
$("#light,#dark").on("click", e => save(html.set(user.theme = e.currentTarget.id)))


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

Vue.component("v-select", VueSelect.VueSelect);

new Vue({
  el: "nav > div",
  template: `
	<div>
		<div id="DocNavi" hidden>
      <div class="doc" v-for="doc in items.docs" :id="doc.path">
        <a :href="doc.path"></svg>{{doc.title}}</a>
        <ol class="sub">
          <li v-for="sub in doc.subs">
            <a :href="sub.path"><svg class="svg" viewBox="0 0 24 24"><use href="/main.svg#chevrons-right"/></svg>{{sub.title}}</a>
            <a v-for="foot in sub.subs" :href="foot.path"><svg class="svg" viewBox="0 0 24 24"><use href="/main.svg#chevrons-right"/></svg><span class="foot">{{foot.title}}</span></a>
          </li>
        </ol>
      </div>
    </div>
	  <div id="APINavi" hidden>
  		<v-select v-model="selectedModule" placeholder="Select Module" :options="items.modules"></v-select>
  		<o-select placeholder="Select Package" model:="root.packages"/>
      <dl>
        <dt>Select kind of Types</dt>
        <dd>
      		<input type="checkbox" id="Interface" value="Interface" v-model="selectedType">
          <label for="Interface">Interface</label>
          <input type="checkbox" id="Functional" value="Functional" v-model="selectedType">
          <label for="Functional">Functional Interface</label>
          <input type="checkbox" id="AbstractClass" value="AbstractClass" v-model="selectedType">
          <label for="AbstractClass">Abstract Class</label>
          <input type="checkbox" id="Class" value="Class" v-model="selectedType">
          <label for="Class">Class</label>
          <input type="checkbox" id="Enum" value="Enum" v-model="selectedType">
          <label for="Enum">Enum</label>
          <input type="checkbox" id="Annotation" value="Annotation" v-model="selectedType">
          <label for="Annotation">Annotation</label>
          <input type="checkbox" id="Exception" value="Exception" v-model="selectedType">
          <label for="Exception">Enum</label>
        </dd>
      </dl>

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

  constructor(type, component) {
    super()
    this.attachShadow({mode: "open"})
    this.root = $(this.shadowRoot).append($("head link[rel=stylesheet]").clone())
    this.properties = new Map();
    
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

  render(item) {
    return item.toString()
  }
  
  connectedCallback() {
    var now = this.root.make("now").on("click", e => list.toggle("open"))
    var list = this.root.make("ol")
    var items = list.make(this.model, item => list.child(this.render(item)))
  }
});


