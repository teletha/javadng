// =====================================================
// Global Event Listener
// =====================================================
document.addEventListener("click", event => {
  var e = event.target;
  if (e.tagName === "A") {
    var path = e.getAttribute("href");
    if (!path.startsWith("http") && !path.startsWith("#") && !path.startsWith(location.pathname)) {
      // handle internal link only
      router.push(path);
      event.preventDefault();
    }
  }
});

// =====================================================
// Define Router
// =====================================================
const router = new VueRouter({
  mode: "history",
  routes: [
    {
      path: "*",
      component: {
        template: "<i/>",
        created: function() {
          this.href();
        },
        watch: {
          $route: "href"
        },
        methods: {
          // ===========================================================
          // Extracts the contents and navigation from the HTML file at
          // the specified path and imports them into the current HTML.
          // ===========================================================
          href: function(to, from) {
            // There is no need to load an external page if the movement is within the same page.
            // However, hash changes should be recorded in the history to enable smooth scrolling.
            if (to && from && to.path == from.path) {
              if (to.hash != from.hash) {
                location.replace(location.hash);
              }
              return;
            }
            
            fetch(this.$route.params.pathMatch)
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

                hljs.highlightAll();
                if (location.hash == "") {
                  // Force scroll to the top of the page if no hash is specified.
                  // However, we want to do this after the document is loaded and rendered,
                  // so we use setTimeout to delay the execution.
                  setTimeout(() => window.scrollTo(0,0), 1);
                } else {
                  // Hash changes should be recorded in the history to enable smooth scrolling.
                  location.replace(location.hash);
                }
              });
          }
        }
      }
    }
  ]
});

// =====================================================
// Define Components
// =====================================================
new Vue({
  el: "main",
  router
});

Vue.component("v-select", VueSelect.VueSelect);

new Vue({
  el: "nav > div",
  template: `
	<div>
	  <div id="DocNavi" v-if="router.currentRoute.path.startsWith('/doc/')">
      <ol>
        <li v-for="doc in items.docs">
          <a v-bind:href="doc.path">{{doc.title}}</a>
          <ol>
            <li v-for="sub in doc.subs">
              <a v-bind:href="sub.path">{{sub.title}}</a>
            </li>
          </ol>
        </li>
      </ol>
    </div>
	  <div id="APINavi" v-if="router.currentRoute.path.startsWith('/api/')">
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
  				<dd v-for="type in filter(package.children)" :class="type.type" v-show="expandAll || package.isOpen" @click="link(type)"><code>{{type.name}}</code></dd>
  			</dl>
  		</div>
    </div>
	</div>
  `,
  data: function() {
    return {
      router: router,
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
    },
    link: function(type) {
      router.push("/api/" + type.packageName + "." + type.name + ".html");
    }
  }
});

// =====================================================
// Live Reload
// =====================================================
if (location.hostname == "localhost") setInterval(() => fetch("http://localhost:9321/live").then(res => {
  if(res.status == 200) location.reload();
}), 3000);