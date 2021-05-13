// =====================================================
// Define Router
// =====================================================
class Router {
  /**
   * This constructor make the configuration and initialization.
   */
  constructor(pathChanged, hashChanged) {
    document.addEventListener("DOMContentLoaded", () => {
      this.pathChanged = pathChanged;
      this.hashChanged = hashChanged;
      this.pathname = location.pathname;
      this.hash = location.hash;
      
      pathChanged();
      hashChanged();
    });
    
    window.addEventListener("popstate", event => this.update());
    document.addEventListener("click", event => {
      var e = event.target;
      if (e.tagName === "A") {
        if (location.origin == e.origin) {
          history.pushState(null, null, e.href);
          this.update();
          event.preventDefault();
        }
      }
    });
    
  }

  update() {
    const that = this;
  
    if (this.pathname == location.pathname) {
      if (this.hash == location.hash) {
        return; // do nothing
      } else {
        this.hash = location.hash;
        this.hashChanged();
        return;
      }
    } else {
      this.pathname = location.pathname;
      this.hash = location.hash;
    
      fetch(location.pathname)
        .then(function(response) {
          return response.text();
        })
        .then(function(html) {
          var start = html.indexOf(">", html.indexOf("<article")) + 1;
          var end = html.lastIndexOf("</article>");
          var article = html.substring(start, end);
          document.querySelector("article").innerHTML = article;

          var start = html.indexOf(">", html.indexOf("<aside")) + 1;
          var end = html.lastIndexOf("</aside>");
          var aside = html.substring(start, end);
          document.querySelector("aside").innerHTML = aside;

          that.pathChanged();
          that.hashChanged();
        });
    }
  }
}

new Router(() => {
  document.getElementById("APINavi").hidden = !location.pathname.startsWith("/api/");
  
  const doc = document.getElementById("DocNavi");
  doc.hidden = !location.pathname.startsWith("/doc/");
  doc.querySelectorAll(":scope > ol > li").forEach(e => {
    if (e.id == location.pathname) {
      e.classList.add("active");
    } else {
      e.classList.remove("active");
    }
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
	<div xmlns:xlink="http://www.w3.org/1999/xlink">
		<div id="DocNavi" hidden>
      <ol class="doc">
        <li v-for="doc in items.docs" :id="doc.path">
          <a :href="doc.path"><svg viewBox="0 0 24 24"><use xlink:href="/main.svg#airplay"/></svg>{{doc.title}}</a>
          <ol class="sub">
            <li v-for="sub in doc.subs">
              <a :href="sub.path">{{sub.title}}</a>
              <ol class="foot">
                <li v-for="foot in sub.subs">
                  <a :href="foot.path"><svg viewBox="0 0 24 24"><use xlink:href="/main.svg#chevrons-right"/></svg>{{foot.title}}</a>
                </li>
              </ol>
            </li>
          </ol>
        </li>
      </ol>
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