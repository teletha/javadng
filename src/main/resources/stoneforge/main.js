// =====================================================
// User Settings
// =====================================================
const
user = JSON.parse(localStorage.getItem("user")) || {},
save = () => localStorage.setItem("user", JSON.stringify(user)),

// =====================================================
// Utilities
// =====================================================
html = document.documentElement,
$ = (q,p) => document.querySelectorAll(q).forEach(p),
svg = (type) => {
  var a = document.createElement("a");
  a.innerHTML = `<svg class="svg" viewBox="0 0 24 24"><use href="/main.svg#${type}"/></svg>`;
  return a;
}


// =====================================================
// View Mode
// =====================================================
html.className = user.theme;
$("#light,#dark", e => e.onclick = () => save(html.className = user.theme = e.id))


// =====================================================
// Enhance code highlight
// =====================================================
hljs.addPlugin({
  "after:highlightElement": ({el, result}) => {
    el.lang = result.language.toUpperCase();
    
    // For some reason, inserting an element in a callback function deletes the inserted element,
    // so I deliberately delay the insertion. Seriously, it makes no sense.
    setTimeout(() => {
      var a = svg("copy");
      a.title = "Copy this code";
      a.onclick = () => navigator.clipboard.writeText(el.textContent);
      
      el.appendChild(a);
    }, 1)
  }
});


// =====================================================
// Dynamic Navigation Indicator
// =====================================================
const navi = new IntersectionObserver(e => {
  e = e.filter(i => i.isIntersecting);
  if (0 < e.length) {
    const i = e.reduce((a,b) => a.intersectionRation > b.intersectionRatio ? a : b);
    if (i) {
      console.log(i.target.id, i.intersectionRatio);
      $("#DocNavi .now", e => e.classList.remove("now"));
      $(`#DocNavi a[href$='#${i.target.id}']`, e => e.classList.add("now"));
    }
  }
}, {root: null, rootMargin: "-40% 0px -60% 0px", threshold: 0})


// =====================================================
// Define Router
// =====================================================
class Router {
  /**
   * This constructor make the configuration and initialization.
   */
  constructor(pathChanged, hashChanged) {
    this.pathChanged = pathChanged;
    this.hashChanged = hashChanged;
    this.path = location.pathname;
    this.hash = location.hash;
      
    document.addEventListener("DOMContentLoaded", () => {
      pathChanged();
      hashChanged();
    });
    
    window.addEventListener("popstate", event => this.update());
    document.addEventListener("click", event => {
      var e = event.target;
      if (e.tagName === "A") {
        if (location.origin == e.origin) {
          if (location.href != e.href) {
            history.pushState(null, null, e.href);
            this.update();
          }
          event.preventDefault();
        }
      }
    });
    
  }

  update() {
    const that = this;
  
    if (this.path == location.pathname) {
      if (this.hash == location.hash) {
        return; // do nothing
      } else {
        this.hash = location.hash;
        this.hashChanged();
        return;
      }
    } else {
      this.path = location.pathname;
      this.hash = location.hash;
    
      fetch(this.path)
        .then(response => {
          return response.text();
        })
        .then(html => {
          $("#Article", e => e.innerHTML = html.substring(html.indexOf(">", html.indexOf("<article")) + 1, html.lastIndexOf("</article>")));
          $("#SubNavi", e => e.innerHTML = html.substring(html.indexOf(">", html.indexOf("<aside")) + 1, html.lastIndexOf("</aside>")));

          that.pathChanged();
          that.hashChanged();
        });
    }
  }
}

new Router(() => {
  // =====================================================
  // Redraw main navigation
  // =====================================================
  $("#APINavi", e => e.hidden = !location.pathname.startsWith("/api/"));
  $("#DocNavi", e => e.hidden = !location.pathname.startsWith("/doc/"));
  $("#DocNavi>div", e => {
    const sub = e.lastElementChild;
    
    if (e.id == location.pathname) {
      e.classList.add("active");
      sub.style.height = sub.scrollHeight + "px";
    } else {
      e.classList.remove("active");
      sub.style.height = 0;
    }
  });
  
  $("#Article section", e => navi.observe(e));

  // =====================================================
  // Initialize metadata icons
  // =====================================================
  $(".perp", e => {
    e.title = "Copy the permanent link";
    e.onclick = () => navigator.clipboard.writeText(location.origin + location.pathname + "#" + e.closest("section").id);
  });
  $(".tweet", e => {
    e.title = "Post this article to Twitter";
    e.href = "https://twitter.com/intent/tweet?url=" + encodeURIComponent(location.origin + location.pathname + "#" + e.closest("section").id) + "&text=" + encodeURIComponent(e.closest("header").firstElementChild.textContent);
    e.target = "_blank";
    e.rel = "noopener noreferrer";
  });
  $(".edit", e => {
    e.title = "Edit this article";
    e.target = "_blank";
    e.rel = "noopener noreferrer";
  });
  
  hljs.highlightAll();
}, () => {
  // scroll to top or #hash
  if (location.hash == "") {
    setTimeout(() => window.scrollTo(0,0), 200); // wait rendering
  } else {
    location.replace(location.hash);
  }
});

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
  		<v-select v-model="selectedPackage" placeholder="Select Package" :options="items.packages"></v-select>

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