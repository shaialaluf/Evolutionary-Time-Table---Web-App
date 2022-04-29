//var chatVersion = 0;
var refreshRate = 1000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("usersList");
var PROBLEM_LIST_URL = buildUrlWithContextPath("problemList");
var problemId = 1;
// var counter =0;
//var CHAT_LIST_URL = buildUrlWithContextPath("chat");

//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
function refreshUsersList(users) {
    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function (index, username) {
        console.log("Adding user #" + index + ": " + username);

        //create a new <li> tag with a value in it and append it to the #userslist (div with id=userslist) element
        $('<li>' + username + '</li>')
            .appendTo($("#userslist"));
    });
}
function refreshProblemsList(problems) {
    //clear all current users
    $("#table-content").empty();

    // rebuild the list of problems
    $.each(problems || [], function (index, problemDetails) {
       // console.log("Adding problem #" + index + ": " + problemDetails);
        buildRowInTable(problemDetails);
    });
}

// //entries = the added chat strings represented as a single string
// function appendToChatArea(entries) {
// //    $("#chatarea").children(".success").removeClass("success");
//
//     // add the relevant entries
//     $.each(entries || [], appendChatEntry);
//
//     // handle the scroller to auto scroll to the end of the chat area
//     var scroller = $("#chatarea");
//     var height = scroller[0].scrollHeight - $(scroller).height();
//     $(scroller).stop().animate({ scrollTop: height }, "slow");
// }

// function appendChatEntry(index, entry){
//     var entryElement = createChatEntry(entry);
//     $("#chatarea").append(entryElement).append("<br>");
// }
//
// function createChatEntry (entry){
//     entry.chatString = entry.chatString.replace (":)", "<img class='smiley-image' src='../../common/images/smiley.png'/>");
//     return $("<span class=\"success\">").append(entry.username + "> " + entry.chatString);
// }

function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function (users) {
            refreshUsersList(users);
        }
    });
}
function  ajaxProblemsList() {
    problemId=1;
    $.ajax({
        url: PROBLEM_LIST_URL,
        success: function (problems) {
            refreshProblemsList(problems);
        }
    });
}
//data: "name=" + $("#nameInput").val(), local8080/contextpath/usersAndProblems/systemDetails
function userCLickedRun() {
    $.ajax({
         data: "problemId="+this[0].id,
         url: this.action,
        timeout: 2000,
        method: 'post',
        error: function () {
            console.log("Failed to submit");
        },
        success: function (algorithmOperation_url) {
            window.location.replace(algorithmOperation_url);
        }
    });

    return false;
}



// send request after choosing xml file
$(function () { // onload...do
    $("#uploadForm").submit(function () {

        var file = this[0].files[0];
        if (file == undefined) {
            $("#result").empty();
            $("#result").append($('<div class="alert alert-danger mt-2"</div>'));
            var errorMessage = "ERROR: " + "You must choose a file before you click upload file!";
            $("#result").children().last().append($('<strong>')).find("strong").text(errorMessage);
        } else if (file.name.substr(file.name.lastIndexOf(".") + 1) !== "xml") {
            $("#result").empty();
            $("#result").append($('<div class="alert alert-danger mt-2"</div>'));
            var errorMessage = "ERROR: " + "This is not a xml file. Please choose xml file";
            $("#result").children().last().append($('<strong>')).find("strong").text(errorMessage);
        } else {
            var formData = new FormData();
            formData.append("fake-key-1", file);
            formData.append("ProblemId", problemId);
            // counter++;
            // if(counter===2){
            //     console.log("sss");
            // }
            // if(counter===3){
            //     console.log("sss");
            // }
            $.ajax({
                method: 'POST',
                data: formData,
                url: this.action,
                processData: false, // Don't process the files
                contentType: false, // Set content type to false as jQuery will tell the server its a query string request
                timeout: 4000,
                error: function (e) {
                    console.log("Failed to submit");
                    var errorMessage = e.responseText;
                    // errorMessage.slice(1,errorMessage.length-2)
                    //  console.log(errorMessage.slice(1, -3));
                    $("#result").empty();
                    // $("#result").text(errorMessage.slice(1, -3));
                    $("#result").append($('<div class="alert alert-danger mt-2"</div>'));
                    var errorMessage = "ERROR: " + errorMessage.slice(1, -3);
                    $("#result").children().last().append($('<strong>')).find("strong").text(errorMessage);
                },
                success: function (problemObject) {


                    $("#result").empty();
                    $("#result").append($('<div class="alert alert-success mt-2"</div>'));
                    $("#result").children().last().append('<strong>File loaded successfully!</strong>')

                    // buildRowInTable(problemObject);

          //           var school = problemObject.systemManager.schoolSettings;
          //           $("#myTable").find("tbody").append($('<tr>'));
          //           var currentRow = $("#myTable").find("tbody").children().last();
          //           currentRow.append($('<td>').text(problemId));
          //           currentRow.append($('<td>').text(problemObject.usernameOwner));
          //           currentRow.append($('<td>').text(school.numberOfDays));
          //           currentRow.append($('<td>').text(school.numberOfHours));
          //           currentRow.append($('<td>').text(school.classRooms.classRooms.length));
          //           currentRow.append($('<td>').text(school.teachers.teachers.length));
          //           currentRow.append($('<td>').text(school.subjects.subjects.length));
          //
          //           var hardRulesAmount = 0;
          //           var softRulesAmount = 0;
          //           for (var i = 0; i < school.rules.rules.length; i++) {
          //               if (school.rules.rules[i].type === "HARD") {
          //                   hardRulesAmount++;
          //               } else if (school.rules.rules[i].type === "SOFT") {
          //                   softRulesAmount++;
          //               }
          //           }
          //
          //           currentRow.append($('<td>').text(softRulesAmount));
          //           currentRow.append($('<td>').text(hardRulesAmount));
          //           currentRow.append($('<td>').text(problemObject.numberOfUsersWhoOperate));
          //           currentRow.append($('<td>').text(0));
          //           currentRow.append($('<td>'));
          //           var buttonId = "button:" + problemId;
          //           currentTd = currentRow.children().last();
          //           currentTd.append($("<form action=''  method='POST'>")).find("form").append($("<input class='btn btn-outline-secondary' type='submit' id='" + buttonId + "' value = 'Run'/>"));
          // //          currentTd.append($("<form action=''  method='POST'>")).find("form").submit(userCLickedRun);
          //
          //           problemId++;
                }
            });
        }

        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    })
});
function buildRowInTable(problemObject){


    var school = problemObject.systemManager.schoolSettings;
    $("#myTable").find("tbody").append($('<tr>'));
    var currentRow = $("#myTable").find("tbody").children().last();
    currentRow.append($('<td>').text(problemId));
    currentRow.append($('<td>').text(problemObject.usernameOwner));
    currentRow.append($('<td>').text(school.numberOfDays));
    currentRow.append($('<td>').text(school.numberOfHours));
    currentRow.append($('<td>').text(school.classRooms.classRooms.length));
    currentRow.append($('<td>').text(school.teachers.teachers.length));
    currentRow.append($('<td>').text(school.subjects.subjects.length));

    var hardRulesAmount = 0;
    var softRulesAmount = 0;
    for (var i = 0; i < school.rules.rules.length; i++) {
        if (school.rules.rules[i].type === "HARD") {
            hardRulesAmount++;
        } else if (school.rules.rules[i].type === "SOFT") {
            softRulesAmount++;
        }
    }

    currentRow.append($('<td>').text(softRulesAmount));
    currentRow.append($('<td>').text(hardRulesAmount));
    currentRow.append($('<td>').text(problemObject.numberOfUsersWhoOperate));
    currentRow.append($('<td>').text(problemObject.bestFitnessOfAll));
    currentRow.append($('<td>'));
    var buttonId = "button:" + problemId;
    currentTd = currentRow.children().last();
    currentTd.append($("<form action='ChangeToAlgorithmOperationServlet'  method='POST'>")).find("form").append($("<input class='btn btn-outline-secondary' type='submit' id='" + buttonId + "' value = 'Run'/>"));
    currentTd.find("form").submit(userCLickedRun);

    problemId++;

}

//call the server and get the chat version
//we also send it the current chat version so in case there was a change
// //in the chat content, we will get the new string as well
// function ajaxChatContent() {
//     $.ajax({
//         url: CHAT_LIST_URL,
//         data: "chatversion=" + chatVersion,
//         dataType: 'json',
//         success: function(data) {
//             /*
//              data will arrive in the next form:
//              {
//                 "entries": [
//                     {
//                         "chatString":"Hi",
//                         "username":"bbb",
//                         "time":1485548397514
//                     },
//                     {
//                         "chatString":"Hello",
//                         "username":"bbb",
//                         "time":1485548397514
//                     }
//                 ],
//                 "version":1
//              }
//              */
//             console.log("Server chat version: " + data.version + ", Current chat version: " + chatVersion);
//             if (data.version !== chatVersion) {
//                 chatVersion = data.version;
//                 appendToChatArea(data.entries);
//             }
//             triggerAjaxChatContent();
//         },
//         error: function(error) {
//             triggerAjaxChatContent();
//         }
//     });
// }

//add a method to the button in order to make that form use AJAX
//and not actually submit the form
// $(function() { // onload...do
//     //add a function to the submit event
//     $("#chatform").submit(function() {
//         $.ajax({
//             data: $(this).serialize(),
//             url: this.action,
//             timeout: 2000,
//             error: function() {
//                 console.error("Failed to submit");
//             },
//             success: function(r) {
//                 //do not add the user string to the chat area
//                 //since it's going to be retrieved from the server
//                 //$("#result h1").text(r);
//             }
//         });
//
//         $("#userstring").val("");
//         // by default - we'll always return false so it doesn't redirect the user.
//         return false;
//     });
// });

// function triggerAjaxChatContent() {
//     setTimeout(ajaxChatContent, refreshRate);
// }

//activate the timer calls after the page is loaded
$(function () {

    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);

    setInterval(ajaxProblemsList, 500);
    //The chat content is refreshed only once (using a timeout) but
    //on each call it triggers another execution of itself later (1 second later)
    //triggerAjaxChatContent();
});


