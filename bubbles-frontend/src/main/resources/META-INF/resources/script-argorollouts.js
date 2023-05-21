const root = document.querySelector("#app");
const stats = document.querySelector("#statistics");
const routes = document.querySelector("#routes");

let { innerHeight, innerWidth } = window;
console.log(innerHeight, innerWidth);
if (innerHeight < 300) {
  innerHeight = 350;
}
if (innerWidth < 300) {
  innerWidth = 750;
}

class Bubble {
  constructor(color) {
    this.bubbleSpan = undefined;
    this.handleNewBubble();
    this.color = color

    this.posY = this.randomNumber(innerHeight - 20, 20);
    this.posX = this.randomNumber(innerWidth - 20, 20);

    this.bubbleSpan.style.top = this.posY + "px";
    this.bubbleSpan.style.left = this.posX + "px";

    // setting height and width of the bubble
    this.height = this.randomNumber(60, 20);
    this.width = this.height;

    this.bubbleEnd.call(this.bubbleSpan, this.randomNumber(6000, 3000));
  }

  // creates and appends a new bubble in the DOM
  handleNewBubble() {
    this.bubbleSpan = document.createElement("span");
    this.bubbleSpan.classList.add("bubble");
    root.append(this.bubbleSpan);
    this.handlePosition();
    this.bubbleSpan.addEventListener("click", this.bubbleEnd);
  }

  handlePosition() {
    // positioning the bubble in different random X and Y
    const randomY = this.randomNumber(60, -60);
    const randomX = this.randomNumber(60, -60);

    this.bubbleSpan.style.backgroundColor = this.color;
    this.bubbleSpan.style.height = this.height + "px";
    this.bubbleSpan.style.width = this.height + "px";

    this.posY = this.randomNumber(innerHeight - 20, 20);
    this.posX = this.randomNumber(innerWidth - 20, 20);

    this.bubbleSpan.style.top = this.posY + "px";
    this.bubbleSpan.style.left = this.posX + "px";

    const randomSec = this.randomNumber(4000, 200);
    setTimeout(this.handlePosition.bind(this), randomSec); // calling for re-position of bubble
  }

  randomNumber(max, min) {
    return Math.floor(Math.random() * (max - min + 1) + min);
  }

  bubbleEnd(removingTime = 0) {
    setTimeout(
      () => {
        requestAnimationFrame(() => this.classList.add("bubble--bust"));
      },
      removingTime = 5000
    );

    setTimeout(() => {
      requestAnimationFrame(() => this.remove());
      requestAnimationFrame(() => new Bubble());
    }, removingTime);
  }
}

// change to /bubble in dev mode
// creating many bubble instance
setInterval(function () {
  requestAnimationFrame(() => {
    fetch('/bubble')
      .then(response => response.json())
      .then(data => new Bubble(data.color))
  });
}, 200);

function createTable(statistics, tablerow) {
  tablerow.innerHTML='';
  let fLen = statistics.length;
  for (let i = 0; i < fLen; i++) {
    let row = document.createElement('td');
    row.innerHTML = statistics[i];
    tablerow.appendChild(row)
  }
}

setInterval(function () {
  fetch("/bubble/vs/bubble-backend")
  .then(response => response.json())
  .then(data => {
    let hosts = data.hosts
    const statistics = []
    for (let hostId in hosts) {
      let host = hosts[hostId]
      let counter = data.stats[host]
      statistics.push(host + " : " + counter)
    }
    createTable(statistics, routes)
  })
}, 200)

// setInterval(function () {
//   fetch("/bubble/statistics")
//   .then(response => response.json())
//   .then(data => {
//     let hosts = data.hosts
//     const statistics = []
//     for (let hostId in hosts) {
//       let host = hosts[hostId]
//       let counter = data.requests[host]
//       statistics.push(host + " : " + counter)
//     }
//     createTable(statistics, stats)
//   })
// }, 400)