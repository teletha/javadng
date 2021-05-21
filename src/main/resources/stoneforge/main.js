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
nop = () => {},
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
// Lightning Fast Viewer
// =====================================================
function FlashMan({paged, preview="section", ...previews}) { 
  var path = location.pathname, hash = location.hash;
  var observer = new IntersectionObserver(set => {
    set.filter(x => x.isIntersecting && !x.target.init && (x.target.init = true)).forEach(x => {
      for (let q in previews) x.target.querySelectorAll(q).forEach(e => previews[q](e))
    })
  }, {rootMargin: "80px", threshold: 0.3});
  
  function update() {
    if (path == location.pathname) {
      if (hash == location.hash) {
        return; // do nothing
      } else {
        hash = location.hash;
        hashed();
        return;
      }
    } else {
      path = location.pathname;
      hash = location.hash;
    
      fetch(path)
        .then(response => {
          return response.text();
        })
        .then(html => {
          $("article", e => e.innerHTML = html.substring(html.indexOf(">", html.indexOf("<article")) + 1, html.lastIndexOf("</article>")));
          $("aside", e => e.innerHTML = html.substring(html.indexOf(">", html.indexOf("<aside")) + 1, html.lastIndexOf("</aside>")));
          updated();
        });
    }
  }
  
  function updated() {
    paged();
    $(preview, e => observer.observe(e));
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
  window.addEventListener("popstate", v => update());
  document.addEventListener("DOMContentLoaded", v => updated());
  document.addEventListener("click", v => {
    let e = v.target;
    if (e.tagName === "A") {
      if (location.origin == e.origin) {
        if (location.href != e.href) {
          history.pushState(null, null, e.href);
          update();
        }
        v.preventDefault();
      }
    }
  });
}

FlashMan({
  paged: () => {
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
  },
  
  preview: "#Article>section",
  /* Enahnce code highlight */
  "pre>code": e => { 
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