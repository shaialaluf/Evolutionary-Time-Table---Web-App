// var currentSchoolDB;

// function saveProblemTables(schoolDB) {
//     currentSchoolDB=schoolDB;
// }

var refreshRate = 100;// if you change you need to change the 99!!!!!
var IntervalFitnessGeneration;
var isPausedClicked = false;

function FlippingJson(maxTupples, component, probability) {

    this.probability = probability;
    this.maxTupples = maxTupples;
    this.component = component;
}

function SizerJson(totalTupples, probability) {

    this.probability = probability;
    this.totalTupples = totalTupples;
}

$(function () {
    $('.pause-button').click(function () {
        isPausedClicked = true;
        clearInterval(IntervalFitnessGeneration);
        $('.resume-button').prop('disabled', false);
        $('.pause-button').prop('disabled', true);
        $('.algo-properties').find("input,select,textarea,button").prop("disabled", false);


        if (!($("#elitismCheckBox").is(':checked'))) {
            $("#elitismTextField").prop('disabled', true);
        }

        $.ajax({
            url: "../usersAndProblems/controlPanel",
            data: {buttonPressed: "pause"},
            method: 'post',
            error: function () {
                console.log("Failed to submit");
            },
            success: function () {

            }
        });
    });
});
$(function () {
    $('.resume-button').click(function () {
        isPausedClicked = false;
        // $('.pause-button').prop('disabled',false);
        // $('.resume-button').prop('disabled',true);

//repass all the data
        var isValid = areAllInputsValidWithoutEndCondition();
        var algorithmParameters = $('#algorithm-property-form').serialize();
        var flippingArr = new Array();
        var sizerArr = new Array();

        $('.flipping-form').each(function (index, curFlipping) {
            var $option = $(curFlipping).find('option:selected');
            var Component = $option.val();//to get content of "value" attrib
            var maxTupples = $(curFlipping).find('.max-tupples-text-field').val();
            if (isNaN(getMaxTupplesNumber($(curFlipping).find('.max-tupples-text-field'), ($(curFlipping).find('.max-tupples-error'))))) {
                isValid = false;
                $(curFlipping).find('.error-label').removeClass('visually-hidden');
            }
            var probability = ($(curFlipping).find('.probability-range-flipping').val());
            flippingArr.push(new FlippingJson(maxTupples, Component, probability));
        });

        $('.sizer-form').each(function (index, curSizer) {
            var totalTupples = $(curSizer).find('.total-tupples-text-field').val();
            if (isNaN(getTotalTupplesNumber($(curSizer).find('.total-tupples-text-field'), ($(curSizer).find('.total-tupples-error'))))) {
                isValid = false;
                $(curSizer).find('.error-label').removeClass('visually-hidden');
            }
            var probability = ($(curSizer).find('.probability-range-sizer').val());
            sizerArr.push(new SizerJson(totalTupples, probability));
        });

        if (isValid === true) {
            $('.generic-error').hide();
            $('.pause-button').prop('disabled', false);
            $('.resume-button').prop('disabled', true);
            var JsonStringFlippingArr = JSON.stringify(flippingArr);
            var JsonStringSizerArr = JSON.stringify(sizerArr);


            $.ajax({
                url: "../usersAndProblems/controlPanel",
                data: {
                    buttonPressed: "resume",
                    flippingArr: JsonStringFlippingArr,
                    sizerArr: JsonStringSizerArr,
                    algorithmParameters: algorithmParameters
                },

                method: 'post',

                error: function () {
                    console.log("Failed to submit");
                },
                success: function () {
                    IntervalFitnessGeneration = setInterval(showAlgorithmResult, refreshRate);
                    $('.algo-properties').find("input,select,textarea,button").prop("disabled", true);


                }
            });
        } else {
            $('.generic-error').show();
        }
    });
});

$(function () {
    $('.stop-button').click(function () {
        $.ajax({
            url: "../usersAndProblems/controlPanel",

            data: {buttonPressed: "stop"},
            method: 'post',
            error: function () {
                console.log("Failed to submit");
            },
            success: function () {
                if (isPausedClicked === true) {
                    IntervalFitnessGeneration = setInterval(showAlgorithmResult, refreshRate);
                }
                isPausedClicked = false;
                $('.resume-button').prop('disabled', true);
                $('.pause-button').prop('disabled', false);
                console.log("stopppppppppp");
            }
        });
    });
});
$(function () {
    $('.show-best-solution-button').click(function () {
        $.ajax({
            url: "../usersAndProblems/controlPanel",
            timeout: 2000,
            data: {buttonPressed: "showBestSolution"},
            method: 'post',
            error: function () {
                console.log("Failed to submit");
            },
            success: function () {

            }
        });
    });
});


$(function () { // onload...do
    $.ajax({
        url: "../usersAndProblems/systemDetails",
        timeout: 2000,
        method: 'post',
        error: function () {
            console.log("Failed to submit");

        },
        success: function (schoolDB) {
            //--------schoolDB---------------------
            // classRooms: {classRooms: Array(4)}
            // numberOfDays: 4
            // numberOfHours: 5
            // rules: {rules: Array(8), softRuleCounter: 5, hardRuleCounter: 3, hardRulesWeight: 80}
            // subjects: {subjects: Array(4)}
            // teachers: {teachers: Array(4)}
            console.log("ffff");
            buildProblemTables(schoolDB);
        }
    });
});


function ClickedRunListener() {
    $('.run-button').click(function () {
        isPausedClicked = false;
        var isValid = areAllInputsValid();

        var algorithmParameters = $('#algorithm-property-form,.end-conditions').serialize();
        var flippingArr = new Array();
        var sizerArr = new Array();

        $('.flipping-form').each(function (index, curFlipping) {
            var $option = $(curFlipping).find('option:selected');
            var Component = $option.val();//to get content of "value" attrib
            var maxTupples = $(curFlipping).find('.max-tupples-text-field').val();
            if (isNaN(getMaxTupplesNumber($(curFlipping).find('.max-tupples-text-field'), ($(curFlipping).find('.max-tupples-error'))))) {
                isValid = false;
                $(curFlipping).find('.error-label').removeClass('visually-hidden');
            }
            var probability = ($(curFlipping).find('.probability-range-flipping').val());
            flippingArr.push(new FlippingJson(maxTupples, Component, probability));
        });

        $('.sizer-form').each(function (index, curSizer) {
            var totalTupples = $(curSizer).find('.total-tupples-text-field').val();
            if (isNaN(getTotalTupplesNumber($(curSizer).find('.total-tupples-text-field'), ($(curSizer).find('.total-tupples-error'))))) {
                isValid = false;
                $(curSizer).find('.error-label').removeClass('visually-hidden');
            }
            var probability = ($(curSizer).find('.probability-range-sizer').val());
            sizerArr.push(new SizerJson(totalTupples, probability));
        });

        if (isValid === true) {
            var JsonStringFlippingArr = JSON.stringify(flippingArr);
            var JsonStringSizerArr = JSON.stringify(sizerArr);
            $('.generic-error').hide();
            ////////////////////////////////////////////// Algorithm run request!!!
            $.ajax({
                url: "../usersAndProblems/algorithmRun",
                data: {
                    flippingArr: JsonStringFlippingArr,
                    sizerArr: JsonStringSizerArr,
                    algorithmParameters: algorithmParameters
                },
                method: 'post',
                timeout: 2000,
                error: function (req, err) {
                    console.log('my message  ' + err);
                },

                success: function () {
                    console.log("switch!!!!");
                    switchToOperationScreen();
                    IntervalFitnessGeneration = setInterval(showAlgorithmResult, refreshRate);
                }
            });

            ////////////////////////////////////////////// Algorithm run request!!!

        } else {
            $('.generic-error').show();
        }
    });
}

$(function () {
////// start operate algorithm
    ClickedRunListener();

});

function showAlgorithmResult() {
    $.ajax({
        url: "../usersAndProblems/algorithmResult",
        method: 'post',
        error: function (req, err) {
            console.log('my message  ' + err);
        },
        success: function (currentResult) {
            console.log("success result");
            if (currentResult.isLast === true || (currentResult.generation === -1 && currentResult.fitness === -1)) {
                if (currentResult.generation !== -1) {
                    updateGenerationAndFitness(currentResult);
                    showBestSolution("true");
                }

                clearInterval(IntervalFitnessGeneration);
                switchToEndCondition();

            } else {
                updateGenerationAndFitness(currentResult);

            }
        }
    });


}

function updateGenerationAndFitness(currentResult) {
    var currentGeneration = currentResult.generation;
    var currentFitness = currentResult.fitness;
    $('.current-generation-label').text(" " + currentGeneration);
    $('.current-fitness-label').text(" " + currentFitness);
}

function switchToEndCondition() {
    //  $('.control-panel-form').remove();
    $('.init-form-end-conditions').show();
    $(".best-solution-component-panel1").show();
    $(".best-solution-component-panel2").show();
    var bestSolutionComponentHTML=$(".control-panel-form").find(".best-solution-component");
    $(".control-panel-form").find(".best-solution-component").remove()
    $('.control-panel-form').hide();
    $('.best-solution-component').show();
    $('.algo-properties').find("input,select,textarea,button").prop("disabled", false);

    if (!$("#elitismCheckBox").is(':checked')) {
        $("#elitismTextField").prop('disabled', true);
    }

   $(".init-form-end-conditions").append(bestSolutionComponentHTML);




//$('#firstRow').append(html);
    // ClickedRunListener();
}

function switchToOperationScreen() {
    $('.control-panel-form').show();
    var bestSolutionComponentHTML=$('.init-form-end-conditions').find(".best-solution-component");
    $('.init-form-end-conditions').find(".best-solution-component").remove()
    $('.init-form-end-conditions').hide();
    $('.algo-properties').find("input,select,textarea,button").prop("disabled", true);

    $('.control-panel-form').append(bestSolutionComponentHTML);
}

function areAllInputsValidWithoutEndCondition() {
    var isAllValid = true;
    if (isNaN(getElitismNumber())) {
        isAllValid = false;
        $('#error-label-elitism').removeClass("visually-hidden");
    }

    if (isNaN(getPopulationNumber())) {
        isAllValid = false;
        $('#error-label-population-size').removeClass("visually-hidden");
    }

    var $optionSelection = $('#selection_selector').find('option:selected');
    var valueOfSelection = $optionSelection.val();
    if (valueOfSelection === "Truncation") {
        if (isNaN(getTopPrecentNumber())) {
            isAllValid = false;
            $('#error-label-top-precent').removeClass("visually-hidden");

        }
    } else if (valueOfSelection === "Tournament") {
        if (isNaN(getPTENumber())) {
            isAllValid = false;
            $('#error-label-pte').removeClass("visually-hidden");

        }
    } else if (valueOfSelection !== "RouletteWheel") {
        isAllValid = false;
        $('#error-label-selection-empty').removeClass('visually-hidden');
    }


    var $optionCrossover = $('#crossover_selector').find('option:selected');
    var valueOfCrossover = $optionCrossover.val();
    if (valueOfCrossover === "DayTimeOriented") {
        if (isNaN(getDayTimeOrientedCuttingPointsNumber())) {
            isAllValid = false;
            $('#error-label-cutting-points-daytimeoriented').removeClass("visually-hidden");

        }
    } else if (valueOfCrossover === "AspectOriented") {
        if (isNaN(getAspectOrientedCuttingPointsNumber())) {
            isAllValid = false;
            $('#error-label-cutting-points-aspectoriented').removeClass("visually-hidden");

        }
    } else {
        isAllValid = false;
        $('#error-label-crossover-empty').removeClass('visually-hidden');
    }


    $('.mutation-select').each(function (index, curMutation) {
        var $optionMutation = $(curMutation).find('option:selected');
        var valueOfMutation = $optionMutation.val()
        if (valueOfMutation !== "Flipping" && valueOfMutation !== "Sizer") {
            isAllValid = false;
            $(curMutation).parent().parent().find('.error-label-mutation-empty').removeClass('visually-hidden');
        }
    });
    return isAllValid;
}


function areAllInputsValid() {
    var isAllValid = true;
    if (isNaN(getElitismNumber())) {
        isAllValid = false;
        $('#error-label-elitism').removeClass("visually-hidden");
    }

    if (isNaN(getPopulationNumber())) {
        isAllValid = false;
        $('#error-label-population-size').removeClass("visually-hidden");
    }

    var $optionSelection = $('#selection_selector').find('option:selected');
    var valueOfSelection = $optionSelection.val();
    if (valueOfSelection === "Truncation") {
        if (isNaN(getTopPrecentNumber())) {
            isAllValid = false;
            $('#error-label-top-precent').removeClass("visually-hidden");

        }
    } else if (valueOfSelection === "Tournament") {
        if (isNaN(getPTENumber())) {
            isAllValid = false;
            $('#error-label-pte').removeClass("visually-hidden");

        }
    } else if (valueOfSelection !== "RouletteWheel") {
        isAllValid = false;
        $('#error-label-selection-empty').removeClass('visually-hidden');
    }


    var $optionCrossover = $('#crossover_selector').find('option:selected');
    var valueOfCrossover = $optionCrossover.val();
    if (valueOfCrossover === "DayTimeOriented") {
        if (isNaN(getDayTimeOrientedCuttingPointsNumber())) {
            isAllValid = false;
            $('#error-label-cutting-points-daytimeoriented').removeClass("visually-hidden");

        }
    } else if (valueOfCrossover === "AspectOriented") {
        if (isNaN(getAspectOrientedCuttingPointsNumber())) {
            isAllValid = false;
            $('#error-label-cutting-points-aspectoriented').removeClass("visually-hidden");

        }
    } else {
        isAllValid = false;
        $('#error-label-crossover-empty').removeClass('visually-hidden');
    }


    $('.mutation-select').each(function (index, curMutation) {
        var $optionMutation = $(curMutation).find('option:selected');
        var valueOfMutation = $optionMutation.val()
        if (valueOfMutation !== "Flipping" && valueOfMutation !== "Sizer") {
            isAllValid = false;
            $(curMutation).parent().parent().find('.error-label-mutation-empty').removeClass('visually-hidden');
        }
    });


    if (getGenerationNumber() === 0 && getFitnessNumber() === 0 && getTimeNumber() === 0) {
        isAllValid = false;
        $('#error-label-conditions-empty').removeClass('visually-hidden');
    }

    if (isNaN(getGenerationNumber())) {
        isAllValid = false;
        $('#error-label-generations').removeClass("visually-hidden");
    }

    if (isNaN(getFitnessNumber())) {
        isAllValid = false;
        $('#error-label-fitness').removeClass("visually-hidden");
    }

    if (isNaN(getTimeNumber())) {
        isAllValid = false;
        $('#error-label-time').removeClass("visually-hidden");
    }

    if (isNaN(getNaturalFrequency())) {
        isAllValid = false;
        $('#error-label-frequency').removeClass("visually-hidden");

    }

    return isAllValid;

}


function buildProblemTables(schoolDB) {
    buildSubjectsTable(schoolDB);
    buildRulesTable(schoolDB);
    buildTeachersTable(schoolDB);
    buildClassesTable(schoolDB);
}

function buildSubjectsTable(schoolDB) {
    $.each(schoolDB.subjects.subjects || [], function (index, subject) {
        $("#Subjects-Table").find("tbody").append($('<tr>'));
        var currentRow = $("#Subjects-Table").find("tbody").children().last();
        currentRow.append($('<td>').text(subject.id));
        currentRow.append($('<td>').text(subject.name));
    });
}


function buildRulesTable(schoolDB) {
    $.each(schoolDB.rules.rules || [], function (index, rule) {
        $("#Rules-Table").find("tbody").append($('<tr>'));
        var currentRow = $("#Rules-Table").find("tbody").children().last();
        currentRow.append($('<td>').text(rule.ruleId));
        currentRow.append($('<td>').text(rule.type));
    });
}

function buildTeachersTable(schoolDB) {
    $.each(schoolDB.teachers.teachers || [], function (mainIndex, teacher) {
        $("#Teachers-Table").find("tbody").append($('<tr>'));
        var numberOfSubjects = teacher.subjects.length;
        var currentRow = $("#Teachers-Table").find("tbody").children().last();
        if (mainIndex % 2 === 0) {
            currentRow.addClass("table-even")
        } else {
            currentRow.addClass("table-odd")
        }
        var nextRow;

        currentRow.append($('<td>').text(teacher.id)).find("td").attr('rowspan', numberOfSubjects);
        currentRow.append($('<td>').text(teacher.name)).find("td").attr('rowspan', numberOfSubjects);

        $.each(teacher.subjects || [], function (index, subject) {
            if (index === 0) {
                currentRow.append($('<td>').text("subject id: " + subject.id + ", subject name: " + subject.name));
            } else {
                $("#Teachers-Table").find("tbody").append($('<tr>'));
                nextRow = $("#Teachers-Table").find("tbody").children().last();
                nextRow.append($('<td>').text("subject id: " + subject.id + ", subject name: " + subject.name));
                if (mainIndex % 2 === 0) {
                    nextRow.addClass("table-even")
                } else {
                    nextRow.addClass("table-odd")
                }
            }

        });


    });

}

function buildClassesTable(schoolDB) {

    $.each(schoolDB.classRooms.classRooms || [], function (mainIndex, classRoom) {
        var subjectCounter = 0;
        $("#Classes-Table").find("tbody").append($('<tr>'));
        var numberOfSubjects = 0;
        $.each(classRoom.subject2WeeklyHours || [], function (index, subject) {
            numberOfSubjects++;
        });
        var currentRow = $("#Classes-Table").find("tbody").children().last();
        if (mainIndex % 2 === 0) {
            currentRow.addClass("table-even")
        } else {
            currentRow.addClass("table-odd")
        }
        var nextRow;

        currentRow.append($('<td>').text(classRoom.id)).find("td").attr('rowspan', numberOfSubjects);
        currentRow.append($('<td>').text(classRoom.name)).find("td").attr('rowspan', numberOfSubjects);

        $.each(classRoom.subject2WeeklyHours || [], function (index, subject) {
            if (subjectCounter === 0) {
                console.log(mainIndex);
                currentRow.append($('<td>').text(index + ", Weekly Hours: " + subject));
                subjectCounter++;
            } else {
                $("#Classes-Table").find("tbody").append($('<tr>'));
                nextRow = $("#Classes-Table").find("tbody").children().last();
                nextRow.append($('<td>').text(index + ", Weekly Hours: " + subject));
                if (mainIndex % 2 === 0) {
                    nextRow.addClass("table-even")
                } else {
                    nextRow.addClass("table-odd")
                }
            }

        });

    });

}

// maybe problem when user load this problem after already run algo!!!!!!!!
$(function () { // onload...do
    $("#elitismTextField").prop('disabled', true);
    $('#elitismCheckBox').change(function () {
        // this will contain a reference to the checkbox
        if (this.checked) {
            // the checkbox is now checked
            $("#elitismTextField").prop('disabled', false);
        } else {
            // the checkbox is now no longer checked
            $('#error-label-elitism').addClass("visually-hidden");
            $("#elitismTextField").val("");
            $("#elitismTextField").prop('disabled', true);
        }
    });

    $("#generations-text-field").prop('disabled', true);
    $('#generations-checkbox').change(function () {
        $('#error-label-conditions-empty').addClass('visually-hidden');
        // this will contain a reference to the checkbox
        if (this.checked) {
            // the checkbox is now checked
            $("#generations-text-field").prop('disabled', false);
        } else {
            // the checkbox is now no longer checked
            $('#error-label-generations').addClass("visually-hidden");
            $("#generations-text-field").val("");
            $("#generations-text-field").prop('disabled', true);
        }
    });


    $("#fitness-text-field").prop('disabled', true);
    $('#fitness-checkbox').change(function () {
        $('#error-label-conditions-empty').addClass('visually-hidden');
        // this will contain a reference to the checkbox
        if (this.checked) {
            // the checkbox is now checked
            $("#fitness-text-field").prop('disabled', false);
        } else {
            // the checkbox is now no longer checked
            $('#error-label-fitness').addClass("visually-hidden");
            $("#fitness-text-field").val("");
            $("#fitness-text-field").prop('disabled', true);
        }
    });


    $("#time-text-field").prop('disabled', true);
    $('#time-checkbox').change(function () {
        $('#error-label-conditions-empty').addClass('visually-hidden');
        // this will contain a reference to the checkbox
        if (this.checked) {
            // the checkbox is now checked
            $("#time-text-field").prop('disabled', false);
        } else {
            // the checkbox is now no longer checked
            $('#error-label-time').addClass("visually-hidden");
            $("#time-text-field").val("");
            $("#time-text-field").prop('disabled', true);
        }
    });
});


$(function () {

    $('#addMutationButton').click(function () {

        var html = '<div class="row form-group circular-frame mutation-frame">' +
            '<form  class="col-sm-6 form-column mutation-type">' +
            '<div class="col-sm-9 label-column">' +
            '<label class="col-form-label">Choose Mutation type</label>' +
            ' </div>' +
            '<div class="col-sm-10 select-column">' +
            '<select name="mutation" class="form-select mutation-select" onchange="mutationTypeChangedInDropDown(event)">' +
            '<option disabled selected value style="display:none">select mutation </option>' +
            '<option value="Flipping"> Flipping</option>' +
            '<option value="Sizer">Sizer</option>' +
            '</select>' +
            '</div>' +
            '<div class="col-sm-10 button-column" >' +
            '<button class="btn btn-light" type="button" onclick="removeMutation(event)">X</button>' +
            '</div>' +
            '<div class="col-sm-12 label-column"><label class="col-form-label error-label error-label-mutation-empty visually-hidden">Error: must choose mutation</label></div>' +
            '</form>' +
            '</div>';


        $('#mutationMainDiv').after(html);


    });
});

function mutationTypeChangedInDropDown(event) {
    var $option = $(event.target).find('option:selected');
    var value = $option.val();//to get content of "value" attrib
    $(event.target).parent().parent().find('.error-label-mutation-empty').addClass('visually-hidden');
    var mutationType;
    if (value === "Flipping") {
        //flippingCounter++;
        mutationType = "Flipping";
        // console.log("flipping flipping flipping");
        var html = '<form class="col-sm-6 form-column flipping-form">' +
            '<div class="col-sm-8 label-column">' +
            '<label class="col-form-label">Component</label>' +
            ' </div>' +
            '<div class="col-sm-12 select-column">' +
            '<select name="component" class="form-select componenet-select">' +
            '<option value="T">T</option>' +
            '<option value="C">C</option>' +
            '<option value="S">S</option>' +
            '<option value="D">D</option>' +
            '<option value="H">H</option>' +
            '</select>' +
            '</div>' +

            '<div class="col-sm-8 label-column">' +
            '<label class="col-form-label">Max Tupples</label>' +
            ' </div>' +

            '<div class="col-sm-12">' +
            '<input name="maxTuppples" class="form-control max-tupples-text-field" type="text" placeholder="Natural Number">' +
            '</div>' +

            '<div class="row form-group">' +
            '  <div class="col-sm-11 label-column"><label class="col-form-label error-label max-tupples-error visually-hidden">Error: enter natural number!</label></div>' +
            '</div>' +

            '<div class="col-sm-8 label-column">' +
            '<label class="col-form-label">Probability</label>' +
            ' </div>' +

            '<div class="col-sm-12">' +
            '<input min="0" max="1" step="0.01" name="probability" class="form-range probability-range-flipping" type="range">' +
            '</div>' +
            '</form>';

        $(event.target).parent().closest('.form-group').find('.sizer-form').remove();

    } else if (value === "Sizer") {
        //sizerCounter++;
        // console.log("sizer sizer sizer");
        mutationType = "Sizer";
        var html = '<form class="col-sm-6 form-column sizer-form">' +
            '<div class="col-sm-8 label-column">' +
            '<label class="col-form-label">Total Tupples</label>' +
            ' </div>' +
            '<div class="col-sm-12">' +
            '<input name="totalTupples" class="form-control total-tupples-text-field" type="text" placeholder="Integer(positive/negative)">' +
            '</div>' +
            '<div class="row form-group">' +
            '  <div class="col-sm-11 label-column"><label class="col-form-label error-label total-tupples-error visually-hidden">Error: must enter Integer!</label></div>' +
            '</div>' +

            '<div class="col-sm-8 label-column">' +
            '<label class="col-form-label">Probability</label>' +
            ' </div>' +

            '<div class="col-sm-12">' +
            '<input min="0" max="1" step="0.01" name="probability" class="form-range probability-range-sizer" type="range">' +
            '</div>' +
            '</form>';

        $(event.target).parent().closest('.form-group').find('.flipping-form').remove();
    }
    $(event.target).parent().closest('.form-group').append(html);
    if (mutationType === "Sizer") {
        checkTotalTuples($(event.target).parent().closest('.form-group').find('.form-control'), $(event.target).parent().closest('.form-group').find('.total-tupples-error'));
    }
    if (mutationType === "Flipping") {
        checkMaxTuples($(event.target).parent().closest('.form-group').find('.form-control'), $(event.target).parent().closest('.form-group').find('.max-tupples-error'));
    }

}

function removeMutation(event) {
    $(event.target).parent().closest('.form-group').remove();
}

function createTournamentHTML() {
    $('#selectionTruncation').remove();
    var html = '<div class="row form-group" id="selectionTournament">' +
        '<div class="col-sm-4 label-column">' +
        '<label class="col-form-label">Enter PTE</label>' +
        '</div>' +
        '<div class="col-sm-6 input-column">' +
        '<input name="pte" class="form-control" id="pte-text-field" type="text" placeholder="Number between 0-1">' +
        '</div>' +
        '<div class="row form-group">' +
        '  <div class="col-sm-9 label-column"><label id="error-label-pte" class="col-form-label error-label visually-hidden">Error: You must enter number between 0-1!</label></div>' +
        '</div>' +
        '</div>';
    $('#selectionMainDiv').after(html);
}

function createTruncationHTML() {
    $('#selectionTournament').remove();
    var html = '<div class="row form-group" id="selectionTruncation">' +
        '<div class="col-sm-4 label-column">' +
        '<label class="col-form-label">Enter top precent</label>' +
        '</div>' +
        '<div class="col-sm-6 input-column">' +
        '<input name ="topPrecent"  id="top-precent-text-field" class="form-control" type="text" placeholder="Integer between 1-100">' +
        '</div>' +
        '<div class="row form-group">' +
        '  <div class="col-sm-9 label-column"><label id="error-label-top-precent" class="col-form-label error-label visually-hidden">Error: You must enter integer between 1-100!</label></div>' +
        '</div>' +
        '</div>';
    $('#selectionMainDiv').after(html);
}

function createRouletteWheelHTML() {
    $('#selectionTruncation').remove();
    $('#selectionTournament').remove();
}

$(function () {
    $('#selection_selector').change(function () {
        $('#error-label-selection-empty').addClass('visually-hidden');
        var $option = $(this).find('option:selected');
        var value = $option.val();//to get content of "value" attrib
        var selectionType;
        if (value === "RouletteWheel") {
            selectionType = "RouletteWheel";
            createRouletteWheelHTML();
        } else if (value === "Truncation") {
            selectionType = "Truncation";
            createTruncationHTML();
        } else if (value === "Tournament") {
            selectionType = "Tournament";
            createTournamentHTML();
        }

        if (selectionType === "Truncation") {
            checkTopPrecent();
        } else if (selectionType === "Tournament") {
            checkPTE();
        }


    });

});


$(function () {
    $('#crossover_selector').change(function () {
        $('#error-label-crossover-empty').addClass('visually-hidden');
        var $option = $(this).find('option:selected');
        var value = $option.val();//to get content of "value" attrib
        var crossoverType;
        if (value === "DayTimeOriented") {
            crossoverType = "DayTimeOriented";
            $('.crossoverAspectOriented').remove();
            var html = '<div class="row form-group" id="crossoverDayTimeOriented">' +
                '<div class="col-sm-4 label-column">' +
                '<label class="col-form-label">Enter number of cutting points</label>' +
                '</div>' +
                '<div class="col-sm-5 input-column">' +
                '<input name="cuttingPoints" class="form-control" id="cutting-points-daytimeoriented-text-field" type="text" placeholder="Natural Number">' +
                '</div>' +
                ' <div class="row form-group">' +
                '<div class="col-sm-8 label-column"><label id="error-label-cutting-points-daytimeoriented" class="col-form-label error-label visually-hidden">Error: You must enter natural number! </label></div>' +
                ' </div>' +
                '</div>';
            $('#crossoverMainDiv').after(html);
        } else if (value === "AspectOriented") {
            crossoverType = "AspectOriented";
            $('#crossoverDayTimeOriented').remove();
            var html = '<div class="row form-group crossoverAspectOriented">' +
                '<div class="col-sm-4 label-column">' +
                '<label class="col-form-label">Enter number of cutting points</label>' +
                '</div>' +
                '<div class="col-sm-5 input-column">' +
                '<input name="cuttingPoints" class="form-control" id="cutting-points-aspectoriented-text-field" type="text" placeholder="Natural Number">' +
                '</div>' +
                ' <div class="row form-group">' +
                '<div class="col-sm-8 label-column"><label id="error-label-cutting-points-aspectoriented" class="col-form-label error-label visually-hidden">Error: You must enter natural number! </label></div>' +
                ' </div>' +
                '<div class="row form-group crossoverAspectOriented">' +
                '<div class="col-sm-5 label-column">' +
                '<label class="col-form-label">Choose orientaion</label>' +
                '</div>' +
                '<div class="col-sm-5 select-column">' +
                '<select name="orientation" id = "crossover_selector_orientation" class = "form-select" >' +
                '<option value ="Teacher"> Teacher </option>' +
                '<option value="Class">Class</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</div>';


            //  '<div class="row form-group crossoverAspectOriented">'+
            //      '<div class="col-sm-5 label-column">'+
            //      '<label class="col-form-label">Choose orientaion</label>'+
            //      '</div>'+
            // '<div class="col-sm-5 select-column">'+
            //  '<select id = "crossover_selector" class = "form-select" >'+
            //      '<option value = "TeacherOriented"> Teacher </option>'+
            //      '<option value="ClassOriented">Class</option>'+
            //  '</select>'+
            //      '</div>'+
            //  '</div>'+
            //  '</div>'


            $('#crossoverMainDiv').after(html);
        }

        if (crossoverType === "DayTimeOriented") {
            checkCuttingPointsDayTime();

        } else if (crossoverType === "AspectOriented") {
            checkCuttingPointsAspect();
        }

    });

});

$(function () {
    $('.back-button').click(function () {
        window.location.replace("../usersAndProblems/usersAndProblems.html");
    });
});

//--------------------------------------------------------------------------------------------------
function isNumber(n) {
    return !isNaN(parseInt(n)) && isFinite(n) && Number.isInteger(Number(n));
}

function getPopulationNumber() {
    var populationNumber = $('#Population-input-field').val();
    if (populationNumber.length === 0)//empty string
    {
        $('#error-label-population-size').addClass("visually-hidden");
        return NaN;
    } else {
        var num;
        if (isNumber(populationNumber)) {
            num = parseInt(populationNumber);
            if (num > 0) {
                $('#error-label-population-size').addClass("visually-hidden");
                return num;
            } else {
                $('#error-label-population-size').removeClass("visually-hidden");
                return NaN;
            }
        } else {
            $('#error-label-population-size').removeClass("visually-hidden");
            return NaN;
        }
    }
}

function getElitismNumber() {

    if ($("#elitismCheckBox").is(':checked')) {

        var elitismNumber = $('#elitismTextField').val();
        if (elitismNumber.length === 0)//empty string
        {
            $('#error-label-elitism').addClass("visually-hidden");
            return NaN;
        } else {
            var num;
            if (isNumber(elitismNumber)) {
                num = parseInt(elitismNumber);
                if (num > 0) {
                    $('#error-label-elitism').addClass("visually-hidden");
                    return num;
                } else {
                    $('#error-label-elitism').removeClass("visually-hidden");
                    return NaN;
                }
            } else {
                $('#error-label-elitism').removeClass("visually-hidden");
                return NaN;
            }
        }
    } else {
        return 0;
    }
}

function isElitistBiggerThanPopulation() {
    var populationNumber = getPopulationNumber();
    var elitismNumber = getElitismNumber();
    if (!isNaN(populationNumber)) {
        if (!isNaN(elitismNumber)) {
            if (populationNumber <= elitismNumber) {
                $('#error-label-elitism').removeClass("visually-hidden");
                return NaN;
            } else {
                return elitismNumber;
            }
        } else {
            return NaN;
        }
    } else {
        return NaN;
    }
}

function getTopPrecentNumber() {
    var topPrecentNumber = $('#top-precent-text-field').val();
    if (topPrecentNumber.length === 0)//empty string
    {
        $('#error-label-top-precent').addClass("visually-hidden");
        return NaN;
    } else {
        var num;
        if (isNumber(topPrecentNumber)) {
            num = parseInt(topPrecentNumber);
            if (num >= 1 && num <= 100) {
                $('#error-label-top-precent').addClass("visually-hidden");
                return num;
            } else {
                $('#error-label-top-precent').removeClass("visually-hidden");
                return NaN;
            }
        } else {
            $('#error-label-top-precent').removeClass("visually-hidden");
            return NaN;
        }
    }
}


function getPTENumber() {
    var PTENumber = $('#pte-text-field').val();
    if (PTENumber.length === 0)//empty string
    {
        $('#error-label-pte').addClass("visually-hidden");
        return NaN;
    } else {
        var num;
        if (!isNaN(parseInt(PTENumber)) && isFinite(PTENumber)) {
            num = parseFloat(PTENumber);
            if (num >= 0 && num <= 1) {
                $('#error-label-pte').addClass("visually-hidden");
                return num;
            } else {
                $('#error-label-pte').removeClass("visually-hidden");
                return NaN;
            }
        } else {
            $('#error-label-pte').removeClass("visually-hidden");
            return NaN;
        }
    }
}


function getDayTimeOrientedCuttingPointsNumber() {
    var cuttingPointsNumber = $('#cutting-points-daytimeoriented-text-field').val();
    if (cuttingPointsNumber.length === 0)//empty string
    {
        $('#error-label-cutting-points-daytimeoriented').addClass("visually-hidden");
        return NaN;
    } else {
        var num;
        if (isNumber(cuttingPointsNumber)) {
            num = parseInt(cuttingPointsNumber);
            if (num > 0) {
                $('#error-label-cutting-points-daytimeoriented').addClass("visually-hidden");
                return num;
            } else {
                $('#error-label-cutting-points-daytimeoriented').removeClass("visually-hidden");
                return NaN;
            }
        } else {
            $('#error-label-cutting-points-daytimeoriented').removeClass("visually-hidden");
            return NaN;
        }
    }
}


function getAspectOrientedCuttingPointsNumber() {
    var cuttingPointsNumber = $('#cutting-points-aspectoriented-text-field').val();
    if (cuttingPointsNumber.length === 0)//empty string
    {
        $('#error-label-cutting-points-aspectoriented').addClass("visually-hidden");
        return NaN;
    } else {
        var num;
        if (isNumber(cuttingPointsNumber)) {
            num = parseInt(cuttingPointsNumber);
            if (num > 0) {
                $('#error-label-cutting-points-aspectoriented').addClass("visually-hidden");
                return num;
            } else {
                $('#error-label-cutting-points-aspectoriented').removeClass("visually-hidden");
                return NaN;
            }
        } else {
            $('#error-label-cutting-points-aspectoriented').removeClass("visually-hidden");
            return NaN;
        }
    }
}

function getTotalTupplesNumber(textField, errorLabel) {
    var totalTupplesNumber = textField.val();
    if (totalTupplesNumber.length === 0)//empty string
    {
        errorLabel.addClass("visually-hidden");
        return NaN;
    } else {
        var num;
        if (isNumber(totalTupplesNumber)) {
            num = parseInt(totalTupplesNumber);
            if (num > 0 || num < 0) {
                errorLabel.addClass("visually-hidden");
                return num;
            } else {
                errorLabel.removeClass("visually-hidden");
                return NaN;
            }
        } else {
            errorLabel.removeClass("visually-hidden");
            return NaN;
        }
    }
}

function getMaxTupplesNumber(textField, errorLabel) {
    var maxTupplesNumber = textField.val();
    if (maxTupplesNumber.length === 0)//empty string
    {
        errorLabel.addClass("visually-hidden");
        return NaN;
    } else {
        var num;
        if (isNumber(maxTupplesNumber)) {
            num = parseInt(maxTupplesNumber);
            if (num > 0) {
                errorLabel.addClass("visually-hidden");
                return num;
            } else {
                errorLabel.removeClass("visually-hidden");
                return NaN;
            }
        } else {
            errorLabel.removeClass("visually-hidden");
            return NaN;
        }
    }
}


function getGenerationNumber() {

    if ($("#generations-checkbox").is(':checked')) {

        var generationNumber = $('#generations-text-field').val();
        if (generationNumber.length === 0)//empty string
        {
            $('#error-label-generations').addClass("visually-hidden");
            return NaN;
        } else {
            var num;
            if (isNumber(generationNumber)) {
                num = parseInt(generationNumber);
                if (num > 100) {
                    $('#error-label-generations').addClass("visually-hidden");
                    return num;
                } else {
                    $('#error-label-generations').removeClass("visually-hidden");
                    return NaN;
                }
            } else {
                $('#error-label-generations').removeClass("visually-hidden");
                return NaN;
            }
        }
    } else {
        return 0;
    }
}


function getFitnessNumber() {

    if ($("#fitness-checkbox").is(':checked')) {

        var fitnessNumber = $('#fitness-text-field').val();
        if (fitnessNumber.length === 0)//empty string
        {
            $('#error-label-fitness').addClass("visually-hidden");
            return NaN;
        } else {
            var num;
            if (!isNaN(parseInt(fitnessNumber)) && isFinite(fitnessNumber)) {
                num = parseFloat(fitnessNumber);
                if (num >= 0 && num <= 100) {
                    $('#error-label-fitness').addClass("visually-hidden");
                    return num;
                } else {
                    $('#error-label-fitness').removeClass("visually-hidden");
                    return NaN;
                }
            } else {
                $('#error-label-fitness').removeClass("visually-hidden");
                return NaN;
            }
        }
    } else {
        return 0;
    }
}


function getTimeNumber() {

    if ($("#time-checkbox").is(':checked')) {

        var timeNumber = $('#time-text-field').val();
        if (timeNumber.length === 0)//empty string
        {
            $('#error-label-time').addClass("visually-hidden");
            return NaN;
        } else {
            var num;
            if (isNumber(timeNumber)) {
                num = parseInt(timeNumber);
                if (num > 0) {
                    $('#error-label-time').addClass("visually-hidden");
                    return num;
                } else {
                    $('#error-label-time').removeClass("visually-hidden");
                    return NaN;
                }
            } else {
                $('#error-label-time').removeClass("visually-hidden");
                return NaN;
            }
        }
    } else {
        return 0;
    }
}

function getNaturalFrequency() {
    var frequencyNumber = $('#frequency-text-field').val();
    if (frequencyNumber.length === 0)//empty string
    {
        $('#error-label-frequency').addClass("visually-hidden");
        return NaN;
    } else {
        var num;
        if (isNumber(frequencyNumber)) {
            num = parseInt(frequencyNumber);
            if (num > 0) {
                $('#error-label-frequency').addClass("visually-hidden");
                return num;
            } else {
                $('#error-label-frequency').removeClass("visually-hidden");
                return NaN;
            }
        } else {
            $('#error-label-frequency').removeClass("visually-hidden");
            return NaN;
        }
    }
}

//population number
$(function () {
    $('#Population-input-field').change(function () {
        isElitistBiggerThanPopulation();
    });
});
//elitism number
$(function () {
    $('#elitismTextField').change(function () {
        isElitistBiggerThanPopulation();
    });
});

// top precent
function checkTopPrecent() {
    $('#top-precent-text-field').change(function () {
        getTopPrecentNumber();
    });
}

//pte
function checkPTE() {
    $('#pte-text-field').change(function () {
        getPTENumber();
    });
}

//cutting points - DayTime
function checkCuttingPointsDayTime() {
    $('#cutting-points-daytimeoriented-text-field').change(function () {
        getDayTimeOrientedCuttingPointsNumber();
    });
}

//cutting points - Aspect
function checkCuttingPointsAspect() {
    $('#cutting-points-aspectoriented-text-field').change(function () {
        getAspectOrientedCuttingPointsNumber();
    });
}


//flipping mutation
function checkMaxTuples(textField, errorLabel) {
    textField.change(function () {
        getMaxTupplesNumber(textField, errorLabel);
    });
}

//sizer mutation
function checkTotalTuples(textField, errorLabel) {
    textField.change(function () {
        getTotalTupplesNumber(textField, errorLabel);
    });
}

$(function () {
    $('#generations-text-field').change(function () {
        getGenerationNumber();
    });
});
$(function () {
    $('#fitness-text-field').change(function () {
        getFitnessNumber();
    });
});
$(function () {
    $('#time-text-field').change(function () {
        getTimeNumber();
    });
});
$(function () {
    $('#frequency-text-field').change(function () {
        getNaturalFrequency();
    });
});


/// dynamic page
$(function () {
    $.ajax({
        url: "../usersAndProblems/pageLoad",
        timeout: 2000,
        method: 'post',
        error: function () {
            console.log("Failed to submit");

        },
        success: function (evolutionaryAlgorithm) {
            if (evolutionaryAlgorithm != null) {
                var x = evolutionaryAlgorithm;
                console.log("evolutionaryAlgorithm!!!!!");
                $('#Population-input-field').val(evolutionaryAlgorithm.initialPopulation);
                if (evolutionaryAlgorithm.SelectionType === "Tournament") {
                    $('#selection_selector').val("Tournament").change();
                    $('#pte-text-field').val(evolutionaryAlgorithm.Selection.pte);

                } else if (evolutionaryAlgorithm.SelectionType === "RouletteWheel") {
                    $('#selection_selector').val("RouletteWheel").change();
                } else if (evolutionaryAlgorithm.SelectionType === "Truncation") {
                    $('#selection_selector').val("Truncation").change();
                    $('#top-precent-text-field').val(evolutionaryAlgorithm.Selection.topPercent);
                    console.log($('#top-precent-text-field').val());
                }

                if (evolutionaryAlgorithm.CrossoverType === "DayTimeOriented") {
                    $('#crossover_selector').val("DayTimeOriented").change();
                    $('#cutting-points-daytimeoriented-text-field').val(evolutionaryAlgorithm.Crossover.cuttingPointsNumber)
                } else if (evolutionaryAlgorithm.CrossoverType === "AspectOriented") {
                    $('#crossover_selector').val("AspectOriented").change();
                    $('#cutting-points-aspectoriented-text-field').val(evolutionaryAlgorithm.Crossover.cuttingPointsNumber);
                    if (evolutionaryAlgorithm.Crossover.type === "CLASS") {
                        $('#crossover_selector_orientation').val("Class").change();
                    } else if (evolutionaryAlgorithm.Crossover.type === "TEACHER") {
                        $('#crossover_selector_orientation').val("Teacher").change();
                    }

                }

                var flippings = evolutionaryAlgorithm.FlippingList;
                var sizers = evolutionaryAlgorithm.SizerList;

                if (flippings.length != 0) {
                    $.each(flippings, (function (index, curFlipping) {
                        $('#addMutationButton').click();
                        var flippingOption = $('.mutation-frame').first();
                        flippingOption.find('.mutation-select').val("Flipping").change();
                        flippingOption.find('.max-tupples-text-field').val(curFlipping.maxTupples);
                        flippingOption.find('.probability-range-flipping').val(curFlipping.probability);
                        flippingOption.find('.componenet-select').val(curFlipping.component).change();
                    }));

                }

                if (sizers.length != 0) {
                    $.each(sizers, (function (index, curSizer) {
                        $('#addMutationButton').click();
                        var sizerOption = $('.mutation-frame').first();
                        sizerOption.find('.mutation-select').val("Sizer").change();
                        sizerOption.find('.total-tupples-text-field').val(curSizer.totalTupples);
                        sizerOption.find('.probability-range-sizer').val(curSizer.probability);
                    }));

                }

                if (evolutionaryAlgorithm.Elitism !== 0) {
                    $('#elitismTextField').val(evolutionaryAlgorithm.Elitism);
                    $('#elitismCheckBox').prop("checked", true);
                }

                switchToOperationScreen();
                if (evolutionaryAlgorithm.isPaused === true) {
                    isPausedClicked = true;
                    $('.resume-button').prop('disabled', false);
                    $('.pause-button').prop('disabled', true);
                    $('.algo-properties').find("input,select,textarea,button").prop("disabled", false);
                    if (!($("#elitismCheckBox").is(':checked'))) {
                        $("#elitismTextField").prop('disabled', true);
                        showAlgorithmResult();
                    }


                } else {
                    IntervalFitnessGeneration = setInterval(showAlgorithmResult, refreshRate);

                }

/// error in this case: run algo + refresh + pause + resume
/// error in server - problem=null (somtimes...)


            }
        }
    });
});

function showCurrentUsersResultTable() {
    $.ajax({
        url: "../usersAndProblems/usersCurrentResult",
        method: 'post',
        error: function () {
            console.log("Failed to submit user table current result");
        },
        success: function (usersCurrentResult) {
            if (usersCurrentResult.length === 0) {
                $('.blank_row').show();
            } else {
                $('.blank_row').hide();
            }
            $("#users-table").find("tbody").children().remove();
            $.each(usersCurrentResult || [], function (index, currentResultListOfUsers) {

                $("#users-table").find("tbody").append($('<tr>'));
                var currentRow = $("#users-table").find("tbody").children().last();
                currentRow.append($('<td>').text(currentResultListOfUsers.username));
                currentRow.append($('<td>').text(currentResultListOfUsers.currentResult.generation));
                currentRow.append($('<td>').text(currentResultListOfUsers.currentResult.fitness));
            });
        }
    });
}

$(function () {
    setInterval(showCurrentUsersResultTable, refreshRate);
});


function createClassesBestSolution(BestSolution) {
    var classesCounter = 0;
    $('.Class-Oriented-Ul').children().remove();
    $('.class-oriented-tabs-content').children().remove();
    $.each(BestSolution.allClassesTimeTables|| [], function (index, classTimeTable) {
        if( index.indexOf(" ")!==-1) {
            var indexWithoutSpace = index.slice(0, index.indexOf(" ")) + index.slice(index.indexOf(" ") + 1);
        }
        else{
            indexWithoutSpace=index;
        }
     //   console.log(index);
        var currenthref = "#" + indexWithoutSpace;
        var html_a_tag = '<li class="nav-item">' +
            '<a class="nav-link a-tag-nav-link" role="tab" data-toggle="tab" aria-selected="false"> </a>' +
            '</li>';
        $(".Class-Oriented-Ul").append(html_a_tag);
        $(".Class-Oriented-Ul").children().last().find('a').attr('href', currenthref);
        $(".Class-Oriented-Ul").children().last().find('a').text(index);
        if (classesCounter === 0) {
            $(".Class-Oriented-Ul").children().last().find('a').addClass('active');
        }


        var htmlTable = '<div role="tabpanel" class="tab-pane">' +
            '<table class="table table-striped table-bordered" style="white-space:pre;"> ' +
            '<thead>' +
            '</thead>' +
            '<tbody>' +
            '</table>' +
            '</div>';

        $(".class-oriented-tabs-content").append(htmlTable);
        if(classesCounter===0){
            $(".class-oriented-tabs-content").children().last().addClass("active");
        }

        $(".class-oriented-tabs-content").children().last().attr('id', indexWithoutSpace);
        var daysNumber = classTimeTable.length;
        var hoursNumber = classTimeTable[0].length;
        var table = $(".class-oriented-tabs-content").children().last().find('table');
        table.find('thead').append('<tr>');
        var firstRow = table.find('thead').find('tr');
        firstRow.append($('<th>').text('Hour/Day'));
        for (var i = 0; i < daysNumber; i++) {
            firstRow.append($('<th>').text(i + 1));
        }
        for (var j = 0; j < hoursNumber; j++) {
            table.find('tbody').append('<tr>');
            var currentRow = table.find('tbody').children().last();
            for (var i = 0; i < daysNumber + 1; i++) {
                currentRow.append('<td>');
                var currentTd = currentRow.children().last();
                if (i === 0) {
                    currentTd.text(j + 1);

                } else {
                    var str = "";
                    if (classTimeTable[i - 1][j] === undefined || classTimeTable[i - 1][j].length < 1) {

                    } else {
                        for (var k = 0; k < classTimeTable[i - 1][j].length; k++) {
                            if (k > 0) {
                                str += "\n\n";
                            }
                            var teacherId = classTimeTable[i - 1][j][k].teacherId;
                            var teacherName = classTimeTable[i - 1][j][k].teacher;
                            var subjectId = classTimeTable[i - 1][j][k].subjectId;
                            var subject = classTimeTable[i - 1][j][k].subject;
                            str += "Teacher id: " + teacherId + "\nTeacher Name: " + teacherName + "\nSubject id: " + subjectId + "\nSubject Name: " + subject;
                        }
                        currentTd.text(str);
                    }
                }
            }
        }
        classesCounter++;
    });
}


function createTeachersBestSolution(BestSolution) {
    var teacherCounter = 0;
    $('.teacher-oriented-ul').children().remove();
    $('.teacher-oriented-tabs-content').children().remove();
    $.each(BestSolution.allTeachersTimeTables || [], function (index, teacherTimeTable) {
        var indexWithoutSpace = index.slice(0, index.indexOf(" ")) + index.slice(index.indexOf(" ") + 1);
        var currenthref = "#" + indexWithoutSpace;
        var html_a_tag = '<li class="nav-item">' +
            '<a class="nav-link a-tag-nav-link" role="tab" data-toggle="tab" aria-selected="false"> </a>' +
            '</li>';
        $(".teacher-oriented-ul").append(html_a_tag);
        $(".teacher-oriented-ul").children().last().find('a').attr('href', currenthref);
        $(".teacher-oriented-ul").children().last().find('a').text(index);
        if (teacherCounter === 0) {
            $(".teacher-oriented-ul").children().last().find('a').addClass('active');
        }


        var htmlTable = '<div role="tabpanel" class="tab-pane">' +
            '<table class="table table-striped table-bordered" style="white-space:pre;"> ' +
            '<thead>' +
            '</thead>' +
            '<tbody>' +
            '</table>' +
            '</div>';

        $(".teacher-oriented-tabs-content").append(htmlTable);
        if(teacherCounter===0){
            $(".teacher-oriented-tabs-content").children().last().addClass("active");
        }

        $(".teacher-oriented-tabs-content").children().last().attr('id', indexWithoutSpace);
        var daysNumber = teacherTimeTable.length;
        var hoursNumber = teacherTimeTable[0].length;
        var table = $(".teacher-oriented-tabs-content").children().last().find('table');
        table.find('thead').append('<tr>');
        var firstRow = table.find('thead').find('tr');
        firstRow.append($('<th>').text('Hour/Day'));
        for (var i = 0; i < daysNumber; i++) {
            firstRow.append($('<th>').text(i + 1));
        }
        for (var j = 0; j < hoursNumber; j++) {
            table.find('tbody').append('<tr>');
            var currentRow = table.find('tbody').children().last();
            for (var i = 0; i < daysNumber + 1; i++) {
                currentRow.append('<td>');
                var currentTd = currentRow.children().last();
                if (i === 0) {
                    currentTd.text(j + 1);

                } else {
                    var str = "";
                    if (teacherTimeTable[i - 1][j] === undefined || teacherTimeTable[i - 1][j].length < 1) {

                    } else {
                        for (var k = 0; k < teacherTimeTable[i - 1][j].length; k++) {
                            if (k > 0) {
                                str += "\n\n";
                            }
                            var classId = teacherTimeTable[i - 1][j][k].classId;
                            var classRoom = teacherTimeTable[i - 1][j][k].classRoom;
                            var subjectId = teacherTimeTable[i - 1][j][k].subjectId;
                            var subject = teacherTimeTable[i - 1][j][k].subject;
                            str += "Class id: " + classId + "\nClass Name: " + classRoom + "\nSubject id: " + subjectId + "\nSubject Name: " + subject;
                        }
                        currentTd.text(str);
                    }
                }
            }
        }
        teacherCounter++;
    });
}




function createRulesBestSolution(BestSolution){
    $('#rules-grades-table-content').children().remove();
    var configuration;
    $.each(BestSolution.ruleGrades || [], function (index, currentRuleGrade) {
        const rulesData=index.split(',');
        var ruleName=rulesData[0].split(':')[1].slice(1);
        var ruleType=rulesData[1].split(':')[1].slice(1);

        if(ruleName==="Sequentiality"){
            configuration="Total Hours: "+BestSolution.totalHourConfiguration;
        }
        else{
            configuration="none";
        }

        $("#Rules-Grades-Table").find('tbody').append('<tr>');
        var currentRow=$("#Rules-Grades-Table").find('tbody').children().last();


        currentRow.append($('<td>').text(ruleName));
        currentRow.append($('<td>').text(ruleType));
        currentRow.append($('<td>').text(configuration));
        currentRow.append($('<td>').text(currentRuleGrade));
    });

}

function createRawBestSolution(BestSolution){
    $('#raw-table-content').children().remove();

    $.each(BestSolution.rawList || [], function (index, currentRawFifth) {
        // const rulesData=index.split(',');
        // var ruleName=rulesData[0].split(':')[1].slice(1);
        // var ruleType=rulesData[1].split(':')[1].slice(1);

        $("#Raw-Table").find('tbody').append('<tr>');
        var currentRow=$("#Raw-Table").find('tbody').children().last();
        currentRow.append($('<td>').text(currentRawFifth.day));
        currentRow.append($('<td>').text(currentRawFifth.hour));
        currentRow.append($('<td>').text(currentRawFifth.classRoom));
        currentRow.append($('<td>').text(currentRawFifth.teacher));
        currentRow.append($('<td>').text(currentRawFifth.subject));
    });

}

function showBestSolution(isLast) {
    $.ajax({
        url: "../usersAndProblems/bestSolution",
        data: {isLast: isLast},
        method: 'post',
        error: function () {
            console.log("Failed to show best solution");
        },
        success: function (BestSolution) {
            var x = BestSolution;
            createTeachersBestSolution(BestSolution);
            createRulesBestSolution(BestSolution);
            createRawBestSolution(BestSolution);
            createClassesBestSolution(BestSolution);
        }
    });
}

$(function () {

    $('.show-best-solution-button').click(function () {
        $(".best-solution-component-panel1").show();
        $(".best-solution-component-panel2").show();
        showBestSolution("false");
    });
});



