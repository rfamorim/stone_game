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
    }, function (data) {
        that.showErrorMessage(data);
    });
}


StoneGame.prototype.drawBoard = function(data) {
    // Erase board
    $('.board-bottom').html('');
    $('.right-big-pit').html('');
    $('.board-top').html('');
    $('.left-big-pit').html('');
    $('.error-message').text('');
    $('.winner-player-message').text('');

    // Set players' names
    $('.current-player-name').text("The current player is: " + data.currentPlayer.name);
    $('.player-1-name').text(data.player1.name);
    $('.player-2-name').text(data.player2.name);

    var numberOfPits = data.board.pitList.length;

    var player1Pits = data.board.pitList.slice(0, numberOfPits/2);
    this.showFilledPits(player1Pits, $(".board-bottom"), $(".right-big-pit"), false);

    var player2Pits = data.board.pitList.slice(numberOfPits/2);
    this.showFilledPits(player2Pits, $(".board-top"), $(".left-big-pit"), true);

    if (data.winnerPlayer != null) {
        $('.winner-player-message').text("The winner is " + data.winnerPlayer.name);
        $("[id^='pit-']").css("pointer-events", "none");
    }
    return;
}

StoneGame.prototype.addNewPit = function(pit, element, pitIndex) {
    var html = `<div class='pit' id='pit-${pitIndex}''><div class='stones'>` + Array(pit.stoneCount+1).join("<div class='stone'></div>") + "</div></div>";
    element.append(html);
    $("#pit-" + pitIndex).on('click', function() {
        game.clickOnPit($(this).prop('id'))
    });
}

StoneGame.prototype.makeAjaxCall = function(url, callType, successCallback, errorCallback) {
    $.ajax({
        url,
        type: callType,
        dataType: 'json',
        success: successCallback,
        error: errorCallback
    });
};

StoneGame.prototype.showFilledPits = function(pitsArray, pitElement, bigPitElement, secondPlayer) {
    var result = [];

    if (!secondPlayer) {
        for (var index = 0; index < pitsArray.length; index++) {
            var pit = pitsArray[index];

            if (pit.bigPit === false) {
                result.push(this.addNewPit(pit, pitElement, index));
            } else {
                result.push(this.addNewPit(pit, bigPitElement, index));
            }
        }
    } else {
        for (var index = pitsArray.length - 1; index >= 0; index--) {
            var pit = pitsArray[index];
            var id = index + pitsArray.length

            if (pit.bigPit === false) {
                result.push(this.addNewPit(pit, pitElement, id));
            } else {
                result.push(this.addNewPit(pit, bigPitElement, id));
            }
        }
    }

    return result;
}

StoneGame.prototype.showErrorMessage = function (errorData) {
    $('.error-message').text('Error: ' + errorData.responseJSON.message);
}

$(function (){
    game.start();
});

var game = new StoneGame("");
// var game = new StoneGame("http://localhost:8080/");