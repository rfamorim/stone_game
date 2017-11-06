function StoneGame (baseUrl) {
    this.baseUrl = baseUrl;
}

StoneGame.prototype.start = function() {
    var that = this;
    this.makeAjaxCall(this.baseUrl + "game", "get", function(data) {
        that.drawBoard(data);
    });
}

StoneGame.prototype.clickOnPit = function(pitId) {
    var that = this;
    var url = this.baseUrl + "move/" + pitId.replace('pit-','');
    var data = this.makeAjaxCall(url, "get", function(data) {
        that.drawBoard(data);
    });
}


StoneGame.prototype.drawBoard = function(data) {
    $('.player-1-name').text(data.player1.name);
    $('.player-2-name').text(data.player2.name);

    var numberOfPits = data.board.pitList.length;

    var player1Pits = data.board.pitList.slice(0, numberOfPits/2);

    $(".board-bottom").html('');
    $(".right-big-pit").html('');
    $(".board-top") .html('');
    $(".left-big-pit").html('');

    this.showFilledPits(player1Pits, $(".board-bottom"), $(".right-big-pit"));

    var player2Pits = data.board.pitList.slice(numberOfPits/2);
    return this.showFilledPits(player2Pits, $(".board-top"), $(".left-big-pit"), true);
}

StoneGame.prototype.addNewPit = function(pit, element, pitIndex) {
    var html = `<div class='pit' id='pit-${pitIndex}''><div class='stones'>` + Array(pit.stoneCount).join("<div class='stone'></div>") + "</div></div>";
    element.append(html);
    $("#pit-" + pitIndex).on('click', function() {
        game.clickOnPit($(this).prop('id'))
    });
}

StoneGame.prototype.makeAjaxCall = function(url, callType, callback) {
    $.ajax({
        url,
        type: callType,
        dataType: 'json',
        success: callback
    });
};

StoneGame.prototype.showFilledPits = function(pitsArray, pitElement, bigPitElement, secondPlayer) {
    if (secondPlayer == null) { secondPlayer = false; }
    return (() => {
        var result = [];
        for (var index = 0; index < pitsArray.length; index++) {
            var pit = pitsArray[index];
            var id = secondPlayer ? index + pitsArray.length : index;

            if (pit.bigPit === false) {
                result.push(this.addNewPit(pit, pitElement, id));
            } else {
                result.push(this.addNewPit(pit, bigPitElement, id));
            }
        }
        return result;
    })();
}

$(function (){



      game.start();
});

var game = new StoneGame("http://localhost:8080/");