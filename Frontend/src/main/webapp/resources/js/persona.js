var personaImages = [
    "../resources/images/persona/001-business-man.svg",
    "../resources/images/persona/002-business-man-1.svg",
    "../resources/images/persona/003-business-woman.svg",
    "../resources/images/persona/004-chef.svg",
    "../resources/images/persona/005-chinese.svg",
    "../resources/images/persona/006-constructor.svg",
    "../resources/images/persona/007-doctor.svg",
    "../resources/images/persona/008-farmer.svg",
    "../resources/images/persona/009-grandfather.svg",
    "../resources/images/persona/010-hipster.svg",
    "../resources/images/persona/011-maid.svg",
    "../resources/images/persona/012-maid-1.svg",
    "../resources/images/persona/013-maid-2.svg",
    "../resources/images/persona/014-maid-3.svg",
    "../resources/images/persona/015-worker.svg",
    "../resources/images/persona/016-worker-1.svg",
    "../resources/images/persona/017-man.svg",
    "../resources/images/persona/018-mechanic.svg",
    "../resources/images/persona/019-mechanic-1.svg",
    "../resources/images/persona/020-professor.svg",
    "../resources/images/persona/021-runner.svg",
    "../resources/images/persona/022-scientist.svg",
    "../resources/images/persona/023-singer.svg",
    "../resources/images/persona/024-student.svg",
    "../resources/images/persona/025-student-1.svg",
    "../resources/images/persona/026-student-2.svg",
    "../resources/images/persona/027-student-3.svg",
    "../resources/images/persona/028-teacher.svg",
    "../resources/images/persona/029-teacher-1.svg",
    "../resources/images/persona/030-teacher-2.svg",
    "../resources/images/persona/031-worker-2.svg",
    "../resources/images/persona/032-pretty.svg",
    "../resources/images/persona/033-teacher-3.svg",
    "../resources/images/persona/034-worker-3.svg",
    "../resources/images/persona/035-worker-4.svg",
    "../resources/images/persona/036-young-man.svg"
];

//default persona
/*var personaProfiles = [
    {
        id: "a77f36ef-92f4-467f-8d13-7df7b5ba422f",
        pic: "../resources/images/persona/001-business-man.svg",
        name: "Marc",
        occupation: "Management",
        age: ["35-44"],
        description: "9-5 Working Employee, Family Father, Owns a Car, healthy lifestyle, environmentally aware, high education.",
        gender: "Male",
        maritalStatus: "Married",
        education: "Academic_Degree",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [3, 2, 5, 4]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [13, 12, 15, 14, 16, 17]
        },
        percentage: 100
    },
    {
        id: "c0bfce83-bbb7-420b-8902-a529bd274acb",
        pic: "../resources/images/persona/024-student.svg",
        name: "Timmy",
        occupation: "Student",
        age: ["18-25"],
        description: "flexible time schedule, Single, Owns a Bike, healthy lifestyle, environmentally aware, high education.",
        gender: "Male",
        maritalStatus: "Married",
        education: "Academic_Degree",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [3, 2, 5, 4]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [13, 12, 15, 14, 16, 17]
        },
        percentage: 0
    },
    {
        id: "321b6c66-89f7-407f-9eb8-e155052f891f",
        pic: "../resources/images/persona/030-teacher-2.svg",
        name: "Anne",
        occupation: "Professional",
        age: ["35-44"],
        description: "8am-3pm Working Employee, Family Mother, Owns a Car, healthy lifestyle, environmentally aware, high education.",
        gender: "Female",
        maritalStatus: "Married",
        education: "Academic_Degree",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [3, 2, 5, 4]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [13, 12, 15, 14, 16, 17]
        },
        percentage: 0
    },
    {
        id: "57e790df-613d-4d6d-becf-1b2917a4e78c",
        pic: "../resources/images/persona/018-mechanic.svg",
        name: "James",
        occupation: "Self-employed",
        age: ["35-44"],
        description: "11am-22pm Working Employee, Family Father, Owns a bike, stressful lifestyle, not environmentally aware, weak education.",
        gender: "Male",
        maritalStatus: "Married",
        education: "Academic_Degree",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [3, 2, 5, 4]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [13, 12, 15, 14, 16, 17]
        },
        percentage: 0
    },
]; */

var personaProfiles = [
    {
        id: "a77f36ef-92f4-467f-8d13-7df7b5ba422f",
        pic: "../resources/images/persona/036-young-man.svg",
        name: "Mike",
        occupation: "Apprenticeship",
        age: ["18-24"],
        description: "",
        gender: "Male",
        maritalStatus: "Single",
        education: "Upper_Secondary_Degree",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [1, 1, 1, 1]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [2, 4, 7, 3, 9, 5]
        },
        percentage: 100
    },
    {
        id: "f21551fe-95b2-4089-8838-35d14da320cb",
        pic: "../resources/images/persona/031-worker-2.svg",
        name: "Christine",
        occupation: "Young_Professional",
        age: ["25-34"],
        description: "",
        gender: "Female",
        maritalStatus: "Relationship",
        education: "Academic_Degree",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [1, 1, 1, 1]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [2, 3, 10, 2, 10, 2]
        },
        percentage: 0
    },
    {
        id: "f8036385-436e-42ec-a3a6-065bd29852cc",
        pic: "../resources/images/persona/015-worker.svg",
        name: "Charlie",
        occupation: "Professional",
        age: ["35-44"],
        description: "",
        gender: "Male",
        maritalStatus: "Single",
        education: "Academic_Degree",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [1, 1, 1, 1]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [2, 6, 5, 5, 6, 5]
        },
        percentage: 0
    },
    {
        id: "50392b44-8024-47ee-a5f9-ba083b432c61",
        pic: "../resources/images/persona/017-man.svg",
        name: "Jerry",
        occupation: "Unemployed",
        age: ["45-54"],
        description: "",
        gender: "Male",
        maritalStatus: "Married",
        education: "Lower_Secondary_Degree",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [1, 1, 1, 1]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [4, 5, 3, 7, 4, 7]
        },
        percentage: 0
    },
    {
        id: "80822293-e552-4cbf-9aaf-1f3752b354a3",
        pic: "../resources/images/persona/035-worker-4.svg",
        name: "Constance",
        occupation: "Young_Professional",
        age: ["35-44"],
        description: "",
        gender: "Female",
        maritalStatus: "Married",
        education: "Post_Secondary_Non-Tertiary",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [1, 1, 1, 1]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [2, 5, 8, 3, 9, 3]
        },
        percentage: 0
    },
    {
        id: "87b64d38-7653-4631-81b9-aded83d9ed2a",
        pic: "../resources/images/persona/004-chef.svg",
        name: "John",
        occupation: "Professional",
        age: ["45-54"],
        description: "",
        gender: "Male",
        maritalStatus: "Married",
        education: "Post_Secondary_Non-Tertiary",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [1, 1, 1, 1]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [3, 4, 8, 4, 9, 4]
        },
        percentage: 0
    },
    {
        id: "574bf242-a82b-4df1-89f5-70f20fd40c41",
        pic: "../resources/images/persona/006-constructor.svg",
        name: "Harald",
        occupation: "Professional",
        age: ["55-64"],
        description: "",
        gender: "Male",
        maritalStatus: "Married",
        education: "Post_Secondary_Non-Tertiary",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [1, 1, 1, 1]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [2, 2, 8, 2, 13, 2]
        },
        percentage: 0
    },
    {
        id: "8ff3c323-bfe7-45b4-9008-7a3231a5125b",
        pic: "../resources/images/persona/029-teacher-1.svg",
        name: "Tim",
        occupation: "Young_Professional",
        age: ["35-44"],
        description: "",
        gender: "Male",
        maritalStatus: "Married",
        education: "Academic_Degree",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [1, 1, 1, 1]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [3, 5, 6, 3, 7, 6]
        },
        percentage: 0
    },
    {
        id: "947cf47b-b642-4e64-bbe0-4fc55f16d07f",
        pic: "../resources/images/persona/013-maid-2.svg",
        name: "Sarah",
        occupation: "Professional",
        age: ["45-54"],
        description: "",
        gender: "Female",
        maritalStatus: "Married",
        education: "Academic_Degree",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [1, 1, 1, 1]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [1, 4, 9, 4, 10, 4]
        },
        percentage: 0
    },
    {
        id: "67070f87-984d-4a06-9ad4-ed334d690919",
        pic: "../resources/images/persona/023-singer.svg",
        name: "Alex",
        occupation: "Management",
        age: ["55-64"],
        description: "",
        gender: "Male",
        maritalStatus: "Married",
        education: "Academic_Degree",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [1, 1, 1, 1]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [2, 2, 6, 2, 10, 4]
        },
        percentage: 0
    },
    {
        id: "0b4fb944-77fa-4731-9b5d-8a859e4d91f9",
        pic: "../resources/images/persona/009-grandfather.svg",
        name: "Jeremy",
        occupation: "Unemployed",
        age: ["65+"],
        description: "",
        gender: "Male",
        maritalStatus: "Separated",
        education: "Post_Secondary_Non-Tertiary",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [1, 1, 1, 1]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [1, 2, 5, 2, 6, 3]
        },
        percentage: 0
    },
    {
        id: "0b4fb944-77fa-4731-9b5d-8a859e4d91f9",
        pic: "../resources/images/persona/034-worker-3.svg",
        name: "Margret",
        occupation: "Unemployed",
        age: ["65+"],
        description: "",
        gender: "Female",
        maritalStatus: "Married",
        education: "Academic_Degree",
        healthProperties: {
            keys: ["Vision", "Hearing", "Physical_Agility", "Cognition"],
            values: [1, 1, 1, 1]
        },
        shoppingProperties: {
            keys: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
            values: [1, 3, 8, 2, 9, 3]
        },
        percentage: 0
    },
];

var healthProperties;
var foodCategories;

function getOntologyProps() {
    if (!isDemo) {
        $.ajax({
            url: 'getOntologyProps',
            type: 'GET',
            success: function (data) {
                ontologyProps = data;
                healthProperties = ontologyProps.censusProps.health;
                foodCategories = ontologyProps.foodCategories;
                console.log("ontologyProps loaded");
            },
            error: function () {
                openModalNotification("An error occured. No Connection To Ontology.");
            }
        });
    }
}


function updatePersonaCards() {
    $("#personaContainer").empty();
    personaProfiles.forEach((persona) => {
        persona.pic = persona.pic.replace("../resources/", "resources/");
        createPersonaCard(persona.id, persona.pic, persona.name, persona.occupation, persona.age, persona.description, persona.healthProperties, persona.gender, persona.education, persona.maritalStatus, persona.percentage);
    })
}

function createPersonaCard(id, pic, name, occupation, age, description, properties, gender, education, maritalStatus, percentage) {

    let cardPart1 = `<div class="personaCard idea-card" id="` + id + `">
                                <div class="editContainer">
                                    <img class="personaEdit" src="resources/images/icons/pencil.svg" onclick="openModalPersonaEdit('` + id + `')">
                                </div>
                                <img class="personaPic" src=` + pic + `>
                                <h2 class="personaName">` + name + `</h2>
                                
                                <span>Age: ` + printAges(age) + `</span><br>
                                <span>Gender: ` + gender + `</span><br>
                                <span>Marital Status: ` + maritalStatus + `</span><br>
                                <span>Occupation:` + occupation + `</span><br>
                                <span>Education: ` + education + `</span>
 
                                <p style="margin-top: 10px;color: #4a5d66;"><b>Health</b></p>
                                <div class="personaPropTagContainer">`;

    let cardPart2 = "";
    properties.keys.forEach(key => {
        let val = properties.values[properties.keys.indexOf(key)];
        cardPart2 = cardPart2 + '<span class="personaPropTag">' + key + ':' + val + '</span>';
    });

    let cardPart3 = `</div>
                                <div class="personaPercentage">
                                    <label for="quantity">
                                        <span>Percentage</span>
                                    </label>
                                    <input id= "percentage-` + id + `" type="number" name="quantity" min="0" max="100" value="` + percentage + `" onchange="updatePersonaPercentage('` + id + `','` + this.value + `' )">
                                </div>
                             </div>`;

    let card = cardPart1 + cardPart2 + cardPart3;


    $("#personaContainer").append(card);
}


/*Carousel*/
$(document).ready(() => {
    const rightArrow = document.querySelector('.nav-arrow-container.pos-right');
    const leftArrow = document.querySelector('.nav-arrow-container.pos-left');
    const cards = document.getElementsByClassName("card-container")[0];
    let currTransform = 0;
    let currIndicator = 1;
    rightArrow.addEventListener("click", () => {
        currTransform = (currTransform - 390 > (-400 * $(".personaCard").length) / 2) ? currTransform - 390 : currTransform;
        cards.style.transform = `translate3d(${currTransform}px, 0, 0)`;
    });
    leftArrow.addEventListener("click", () => {
        currTransform = (currTransform + 390 < (400 * $(".personaCard").length) / 2) ? currTransform + 390 : currTransform;
        cards.style.transform = `translate3d(${currTransform}px, 0, 0)`;
    });
});

function printAges(age) {
    var ages = "";
    for (let i = 0; i < age.length; i++) {
        ages = ages + " " + age[i] + ";"
    }
    return ages;
}

function updatePersonaPercentage(id) {
    let inputID = "percentage-" + id;
    personaProfiles.filter(profile => profile.id == id)[0].percentage = parseInt(document.getElementById(inputID).value)
}