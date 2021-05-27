function template(blueprint) {
  let instructions = [];
  let builder = [];
  blueprint(capture(builder, instructions), {items:["1", "2"], has:true})
  console.log(builder)
  console.log(instructions)

  const root = $("<template>")
  instructions.reduce((prev, next) => {
    var result = next(prev)
    console.log(result.toString())
    return result
  }, root)
  console.log(root.toString())

  return (...args) => {
  }
}

function capture(builder, instructions) {
  const capture = {
    get: (target, name) => {
      // field
      if (typeof name !== "symbol") {
        builder.push("Create element " + name)
        instructions.push(c => c.make(name))
      }

      return new Proxy(()=>{}, {
        get: capture.get,
        apply: (target, that, args) => {
          // remove field access info
          builder.pop()
          instructions.pop()

          // method
          if (name === "each") {
            args[0].forEach(args[1])
          } else if (name === "when") {
            builder.push("IF-THEN")
            instructions.push(c => c)
            args[1]()
            builder.push("ELSE")
            instructions.push(c => c)
            args[2]()
            builder.push("END")
            instructions.push(c => c)
          } else if (name === "text") {
            builder.push("Create text[" + args[0] + "]")
            instructions.push(c => c)
          } else if (typeof name === "symbol") {
            builder.push(args[0] === "number" ? "CHILD" : "PARENT")
            instructions.push(c => c)
            return 1;
          } else {
            builder.push("@" + name + "='" + args[0] + "'")
            instructions.push(c => c)
            //instructions.push(c => c.attr(name, args[0]))
          }
          return new Proxy(()=>{}, capture)
        }
      })
    }
  }
  return new Proxy({}, capture);
}


template((h, model) => {
  +h.view.click(this.toggle)
    +h.h2.text(this.placeholder)
      .svg.use("/main.svg#x").onclick(this.select)
      .svg.use("/main.svg#chevron")
  -h.section
    +h.p.text("next section")
    .p.text("multiple paragraph")
  -h.ol
    +h.each(model.items, item => {
      h.li.text("No" + item)
       .li.text("Extra" + item)
    })
    .when(model.has, () => {
      h.li.text("TRUE")
    }, () => {
      h.li.text("FALSE")
    })
    .li.text("END")
  -h.section
    +h.p.text("last section")
})
