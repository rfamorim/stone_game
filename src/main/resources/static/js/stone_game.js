class StoneGame {
  constructor(baseUrl) {
    this.baseUrl = baseUrl;
  }

  start() {
    const data = this.makeAjaxCall(this.baseUrl, "get");
    return this.drawBoard(data);
  }

  clickOnPit() {
    return $(".pit").click(function() {
      const url = this.baseUrl + "/move/" + this.id.replace('pit-','');
      const data = this.makeAjaxCall(url, "get");
      return this.drawBoard(data);
    });
  }

  drawBoard(data) {
    $('.player-1-name').text(data.player1.name);
    $('.player-2-name').text(data.player2.name);

    const numberOfPits = data.board.pitList.length;

    const player1Pits = data.board.pitList.slice(0, numberOfPits/2);
    this.showFilledPits(player1Pits, $(".board-bottom"), $(".right-big-pit"));

    const player2Pits = data.board.pitList.slice(numberOfPits/2);
    return this.showFilledPits(player2Pits, $(".board-top"), $(".left-big-pit"), true);
  }

  addNewPit(pit, element, pitIndex) {
    const html = `<div class='pit' id='pit-${pitIndex}''><div class='stones'>` + Array(pit.stoneCount).join("<div class='stone'></div>") + "</div></div>";

    return element.append(html);
  }

  makeAjaxCall(url, callType, data = null) {
    return $.ajax({
      url,
      type: callType,
      dataType: 'json',
      data
    });
  }

  showFilledPits(pitsArray, pitElement, bigPitElement, secondPlayer) {
    if (secondPlayer == null) { secondPlayer = false; }
    return (() => {
      const result = [];
      for (let index = 0; index < pitsArray.length; index++) {
        const pit = pitsArray[index];
        const id = secondPlayer ? index + pitsArray.length : index;

        if (pit.bigPit === false) {
          result.push(this.addNewPit(pit, pitElement, id));
        } else {
          result.push(this.addNewPit(pit, bigPitElement, id));
        }
      }
      return result;
    })();
  }
}

this.StoneGame = new StoneGame("http://10.0.0.177:8080/game");