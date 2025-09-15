package com.example.jagruk.data


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DoNotStep
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Water
import com.example.jagruk.data.models.*

object HardcodedData {

    // Sample User Profile
    val sampleUser = UserProfile(
        id = "user_001",
        name = "Arjun Sharma",
        email = "arjun@example.com",
        location = "Indore, Madhya Pradesh",
        registrationDate = System.currentTimeMillis(),
        progress = UserProgress(
            completedModules = setOf("landslide_module"),
            moduleProgress = mapOf(
                "earthquake_module" to 3,
                "landslide_module" to 8
            ),
            earnedBadges = listOf(
                Badge(
                    id = "landslide_expert",
                    title = "Landslide Expert",
                    description = "Completed Landslide Safety Module",
                    iconRes = Icons.Default.Landscape,
                    earnedDate = System.currentTimeMillis() - 86400000
                ),
                Badge(
                    id = "first_steps",
                    title = "First Steps",
                    description = "Started your safety journey",
                    iconRes = Icons.Default.DoNotStep,
                    earnedDate = System.currentTimeMillis() - 172800000
                )
            ),
            totalScore = 450,
            streak = 3
        )
    )

    // Earthquake Module Quizzes
    private val earthquakeQuiz1 = Quiz(
        id = "earthquake_quiz_1",
        title = "Understanding Earthquakes",
        questions = listOf(
            QuizQuestion(
                id = "eq_q1",
                question = "What is the primary cause of ground shaking during an earthquake?",
                options = listOf(
                    "Heavy rainfall",
                    "The release of energy from moving tectonic plates",
                    "Strong winds",
                    "Volcanic eruptions"
                ),
                correctAnswerIndex = 1,
                explanation = "Earthquakes are caused when tectonic plates release energy as they move past each other."
            ),
            QuizQuestion(
                id = "eq_q2",
                question = "Which of the following is a secondary hazard of earthquakes?",
                options = listOf(
                    "Ground shaking",
                    "Fire caused by broken gas lines",
                    "Surface rupture",
                    "The sound of the earthquake"
                ),
                correctAnswerIndex = 1,
                explanation = "Fires from broken gas lines are secondary hazards triggered by the primary earthquake shaking."
            ),
            QuizQuestion(
                id = "eq_q3",
                question = "The most common cause of injury inside buildings during earthquakes is:",
                options = listOf(
                    "The building collapsing completely",
                    "Falling objects and non-structural elements",
                    "People panicking and running",
                    "Electrical shock from damaged wires"
                ),
                correctAnswerIndex = 1,
                explanation = "Most injuries come from falling objects like ceiling fans, bookshelves, and light fixtures rather than building collapse."
            ),
            QuizQuestion(
                id = "eq_q4",
                question = "What should you do the moment you feel earthquake shaking?",
                options = listOf(
                    "Run outside immediately",
                    "Call for help",
                    "Wait for teacher's instruction",
                    "Drop, Cover, and Hold On"
                ),
                correctAnswerIndex = 3,
                explanation = "Drop, Cover, and Hold On is the immediate life-saving action to protect from falling objects."
            )
        )
    )

    private val earthquakeQuiz2 = Quiz(
        id = "earthquake_quiz_2",
        title = "Evacuation and Response",
        questions = listOf(
            QuizQuestion(
                id = "eq_q5",
                question = "After earthquake shaking stops, what should you do first?",
                options = listOf(
                    "Immediately run out of the classroom",
                    "Stay in position and assess surroundings for 10 seconds",
                    "Start cleaning up the classroom",
                    "Take photos for social media"
                ),
                correctAnswerIndex = 1,
                explanation = "Take time to assess for hazards like fallen objects or injured people before moving."
            ),
            QuizQuestion(
                id = "eq_q6",
                question = "If you feel an aftershock during evacuation, you should:",
                options = listOf(
                    "Run faster to get outside",
                    "Ignore it and keep walking",
                    "Drop, Cover, and Hold On until shaking stops",
                    "Huddle with friends"
                ),
                correctAnswerIndex = 2,
                explanation = "Aftershocks require the same Drop, Cover, Hold On response as the main earthquake."
            )
        )
    )

    // Landslide Module Quizzes
    private val landslideQuiz1 = Quiz(
        id = "landslide_quiz_1",
        title = "Landslide Basics",
        questions = listOf(
            QuizQuestion(
                id = "ls_q1",
                question = "What is the most common trigger for landslides?",
                options = listOf(
                    "Earthquakes",
                    "Deforestation",
                    "Water saturation from heavy rain",
                    "Volcanic eruptions"
                ),
                correctAnswerIndex = 2,
                explanation = "Water saturation from rain or snowmelt is the primary trigger, weakening soil stability."
            ),
            QuizQuestion(
                id = "ls_q2",
                question = "Which warning sign indicates potential landslide activity?",
                options = listOf(
                    "Clear sunny weather",
                    "Doors and windows suddenly sticking or jamming",
                    "Decrease in bird sounds",
                    "Ground feeling unusually firm"
                ),
                correctAnswerIndex = 1,
                explanation = "Structural changes like sticking doors indicate ground movement and instability."
            ),
            QuizQuestion(
                id = "ls_q3",
                question = "A 'Landslide Warning' from authorities means:",
                options = listOf(
                    "Landslides are possible, monitor the situation",
                    "Landslides are occurring or imminent, evacuate immediately",
                    "General potential exists in coming days",
                    "The risk has passed"
                ),
                correctAnswerIndex = 1,
                explanation = "A Warning means immediate danger - evacuate the area right away."
            )
        )
    )

    // Learning Modules
    val learningModules = listOf(
        LearningModule(
            id = "earthquake_module",
            title = "Earthquake Preparedness",
            description = "Learn how to stay safe before, during, and after earthquakes",
            iconRes = Icons.Default.Warning,
            status = ModuleStatus.IN_PROGRESS,
            estimatedTimeMinutes = 25,
            pages = listOf(
                ModulePage(
                    id = "eq_page_1",
                    title = "What Happens During an Earthquake?",
                    type = PageType.CONTENT,
                    content = """
                        An earthquake is the sudden shaking of the Earth's surface caused by the movement of tectonic plates. 
                        
                        **Primary Hazards:**
                        • Ground Shaking - from gentle rumble to violent jolts
                        • Surface Rupture - ground cracks along fault lines
                        
                        **Secondary Hazards:**
                        • Falling Objects - biggest danger inside buildings
                        • Landslides - shaking destabilizes slopes  
                        • Fires - from broken gas lines and electrical wires
                        • Liquefaction - wet soil behaves like liquid
                        
                        Your goal is to protect yourself from falling objects and structural collapse.
                    """.trimIndent()
                ),
                ModulePage(
                    id = "eq_page_2",
                    title = "Alert Systems & Immediate Response",
                    type = PageType.CONTENT,
                    content = """
                        **The Drop, Cover, Hold On Protocol:**
                        
                        🔻 **DROP** to the floor immediately
                        🛡️ **COVER** your head and neck under a sturdy desk
                        ✋ **HOLD ON** to your shelter until shaking stops
                        
                        **Remember:** 
                        • Don't wait for announcements - act when you feel shaking
                        • If no desk available, cover head against interior wall
                        • Stay away from windows and tall furniture
                        • Practice this response until it becomes automatic
                    """.trimIndent()
                ),
                ModulePage(
                    id = "eq_page_3",
                    title = "Knowledge Check",
                    type = PageType.QUIZ,
                    content = "",
                    quiz = earthquakeQuiz1
                ),
                ModulePage(
                    id = "eq_page_4",
                    title = "After the Shaking Stops",
                    type = PageType.CONTENT,
                    content = """
                        **Immediate Actions:**
                        1. **Wait & Assess** - Stay in safe position, watch for aftershocks
                        2. **Look Around** - Check for injuries, fallen objects, broken glass
                        3. **Prepare to Evacuate** - Listen for instructions, grab essentials only
                        
                        **What to Look For:**
                        • Injured people needing help
                        • Blocked exits or dangerous debris
                        • Signs of fire or electrical hazards
                        • Structural damage to the building
                    """.trimIndent()
                ),
                ModulePage(
                    id = "eq_page_5",
                    title = "Safe Evacuation Procedures",
                    type = PageType.CONTENT,
                    content = """
                        **Four Golden Rules:**
                        • Don't Talk! 
                        • Don't Push!
                        • Don't Run!
                        • Don't Turn Back!
                        
                        **Evacuation Steps:**
                        1. Follow your teacher's lead
                        2. Use designated evacuation routes
                        3. Move in buddy pairs if instructed
                        4. Go directly to Emergency Assembly Area
                        5. Stay with your class for roll call
                        
                        **Key Point:** Move away from the building to avoid falling debris.
                    """.trimIndent()
                ),
                ModulePage(
                    id = "eq_page_6",
                    title = "Response Check",
                    type = PageType.QUIZ,
                    content = "",
                    quiz = earthquakeQuiz2
                ),
                ModulePage(
                    id = "eq_page_7",
                    title = "Hazard Hunt - Making Spaces Safer",
                    type = PageType.CONTENT,
                    content = """
                        Become a "Risk Detective" by identifying non-structural hazards:
                        
                        **Common Classroom Hazards:**
                        • Tall furniture not bolted to walls
                        • Heavy objects on high shelves
                        • Computers that can slide off desks
                        • Hanging pictures or ceiling fans
                        • Chemicals on open shelves
                        • Items blocking exit routes
                        
                        **Simple Solutions:**
                        • Move heavy items to lower shelves
                        • Secure furniture with wall brackets
                        • Store chemicals in locked cabinets
                        • Keep emergency exits clear
                    """.trimIndent()
                )
            )
        ),

        LearningModule(
            id = "landslide_module",
            title = "Landslide Safety",
            description = "Recognize warning signs and respond to landslide threats",
            iconRes = Icons.Default.Landscape,
            status = ModuleStatus.COMPLETED,
            estimatedTimeMinutes = 20,
            pages = listOf(
                ModulePage(
                    id = "ls_page_1",
                    title = "What is a Landslide?",
                    type = PageType.CONTENT,
                    content = """
                        A landslide is the movement of rock, earth, or debris down a slope. They range from slow "creeps" to fast, destructive debris flows.
                        
                        **Main Triggers:**
                        • **Water Saturation** - most common cause
                        • **Earthquakes** - shaking destabilizes slopes
                        • **Human Activities** - construction, deforestation
                        • **Volcanic Activity** - creates debris flows
                        
                        **Types of Movement:**
                        • Falls - rocks detaching from cliffs
                        • Topples - forward rotation around pivot point
                        • Slides - movement along slip surface  
                        • Flows - water-debris mixture moving like fluid
                    """.trimIndent()
                ),
                ModulePage(
                    id = "ls_page_2",
                    title = "Warning Signs & Alerts",
                    type = PageType.CONTENT,
                    content = """
                        **Landscape Changes:**
                        • New cracks in walls, foundations, or ground
                        • Tilting fences, utility poles, or trees
                        • Bulging ground at slope base
                        • Doors/windows sticking for first time
                        
                        **Water Flow Changes:**
                        • Stream water turning from clear to muddy
                        • New springs appearing in ground
                        • Sudden increase or decrease in water flow
                        
                        **Sounds:**
                        • Faint rumbling that gets louder
                        • Trees cracking or boulders knocking
                        
                        **Official Alerts:**
                        • Advisory - potential risk based on weather
                        • Watch - landslides possible, monitor conditions  
                        • Warning - occurring now, evacuate immediately
                    """.trimIndent()
                ),
                ModulePage(
                    id = "ls_page_3",
                    title = "Knowledge Assessment",
                    type = PageType.QUIZ,
                    content = "",
                    quiz = landslideQuiz1
                )
            )
        ),

        LearningModule(
            id = "flood_module",
            title = "Flood Safety",
            description = "Prepare for and respond to flood emergencies",
            iconRes = Icons.Default.Water,
            status = ModuleStatus.NOT_STARTED,
            estimatedTimeMinutes = 30,
            pages = listOf(
                ModulePage(
                    id = "flood_page_1",
                    title = "Understanding Floods",
                    type = PageType.CONTENT,
                    content = "Content about flood types, causes, and risks..."
                )
            )
        ),

        LearningModule(
            id = "cyclone_module",
            title = "Cyclone Preparedness",
            description = "Stay safe during cyclone and severe weather events",
            iconRes = Icons.Default.Air,
            status = ModuleStatus.NOT_STARTED,
            estimatedTimeMinutes = 35,
            pages = listOf(
                ModulePage(
                    id = "cyclone_page_1",
                    title = "Cyclone Basics",
                    type = PageType.CONTENT,
                    content = "Content about cyclone formation and hazards..."
                )
            )
        ),

        LearningModule(
            id = "first_aid_module",
            title = "Emergency First Aid",
            description = "Basic first aid skills for disaster situations",
            iconRes = Icons.Default.Add,
            status = ModuleStatus.NOT_STARTED,
            estimatedTimeMinutes = 40,
            pages = listOf(
                ModulePage(
                    id = "first_aid_page_1",
                    title = "First Aid Fundamentals",
                    type = PageType.CONTENT,
                    content = "Content about basic first aid procedures..."
                )
            )
        )
    )

    // Alerts Data
    val currentAlerts = listOf(
        Alert(
            id = "alert_001",
            title = "Heavy Rainfall Warning",
            description = "Heavy to very heavy rainfall expected in Indore district. Landslide risk elevated in hilly areas.",
            type = AlertType.LANDSLIDE,
            severity = AlertSeverity.HIGH,
            location = "Indore, Madhya Pradesh",
            timestamp = System.currentTimeMillis() - 3600000,
            isActive = true,
            actionItems = listOf(
                "Avoid traveling to hilly areas",
                "Stay alert for ground movement signs",
                "Keep emergency kit ready",
                "Follow local authority instructions"
            )
        ),
        Alert(
            id = "alert_002",
            title = "Heat Wave Advisory",
            description = "Temperatures expected to reach 42°C. Stay hydrated and avoid outdoor activities during peak hours.",
            type = AlertType.HEAT_WAVE,
            severity = AlertSeverity.MODERATE,
            location = "Central MP Region",
            timestamp = System.currentTimeMillis() - 7200000,
            isActive = true,
            actionItems = listOf(
                "Drink plenty of water",
                "Wear light-colored, loose clothing",
                "Stay indoors during 11 AM - 4 PM",
                "Check on elderly neighbors"
            )
        )
    )

    // Emergency Kit Items
    val emergencyKitItems = listOf(
        EmergencyKitItem(
            id = "kit_001",
            name = "Water (1 gallon per person per day)",
            description = "Store at least 3 days worth of water",
            category = "Basic Needs",
            isChecked = true,
            isPriority = true
        ),
        EmergencyKitItem(
            id = "kit_002",
            name = "Non-perishable food (3-day supply)",
            description = "Canned goods, dry cereals, peanut butter",
            category = "Basic Needs",
            isChecked = true,
            isPriority = true
        ),
        EmergencyKitItem(
            id = "kit_003",
            name = "Flashlight with extra batteries",
            description = "LED flashlight preferred, avoid candles",
            category = "Tools",
            isChecked = false,
            isPriority = true
        ),
        EmergencyKitItem(
            id = "kit_004",
            name = "First Aid Kit",
            description = "Bandages, antiseptic, medications",
            category = "Medical",
            isChecked = false,
            isPriority = true
        ),
        EmergencyKitItem(
            id = "kit_005",
            name = "Emergency Radio",
            description = "Battery-powered or hand crank radio",
            category = "Communication",
            isChecked = false,
            isPriority = true
        ),
        EmergencyKitItem(
            id = "kit_006",
            name = "Important Documents (copies)",
            description = "ID, insurance, bank records in waterproof container",
            category = "Documents",
            isChecked = false,
            isPriority = true
        ),
        EmergencyKitItem(
            id = "kit_007",
            name = "Cash and Credit Cards",
            description = "Small bills and coins",
            category = "Financial",
            isChecked = false,
            isPriority = false
        ),
        EmergencyKitItem(
            id = "kit_008",
            name = "Sleeping Bags and Blankets",
            description = "One per person",
            category = "Shelter",
            isChecked = false,
            isPriority = false
        ),
        EmergencyKitItem(
            id = "kit_009",
            name = "Change of Clothing",
            description = "Sturdy shoes, rain gear, hat and gloves",
            category = "Personal",
            isChecked = false,
            isPriority = false
        ),
        EmergencyKitItem(
            id = "kit_010",
            name = "Fire Extinguisher",
            description = "ABC type extinguisher",
            category = "Safety",
            isChecked = false,
            isPriority = false
        )
    )

    // Emergency Contacts
    val emergencyContacts = listOf(
        EmergencyContact(
            id = "contact_001",
            name = "Police",
            phoneNumber = "100",
            relationship = "Emergency Service",
            isPrimary = true
        ),
        EmergencyContact(
            id = "contact_002",
            name = "Fire Department",
            phoneNumber = "101",
            relationship = "Emergency Service",
            isPrimary = true
        ),
        EmergencyContact(
            id = "contact_003",
            name = "Ambulance",
            phoneNumber = "102",
            relationship = "Medical Emergency",
            isPrimary = true
        ),
        EmergencyContact(
            id = "contact_004",
            name = "Papa",
            phoneNumber = "+91 9876543210",
            relationship = "Father",
            isPrimary = true
        ),
        EmergencyContact(
            id = "contact_005",
            name = "Mama",
            phoneNumber = "+91 9876543211",
            relationship = "Mother",
            isPrimary = true
        )
    )

    // Emergency Shelters
    val emergencyShelters = listOf(
        EmergencyShelter(
            id = "shelter_001",
            name = "Government High School, Vijay Nagar",
            address = "Scheme No. 54, Vijay Nagar, Indore",
            coordinates = 22.7520 to 75.8937,
            capacity = 500,
            facilities = listOf("Water Supply", "Toilets", "First Aid", "Power Backup"),
            contactNumber = "+91 731 2234567",
            distanceKm = 2.3,
            isGovernmentApproved = true
        ),
        EmergencyShelter(
            id = "shelter_002",
            name = "Municipal Corporation Community Center",
            address = "Geeta Bhawan Area, Indore",
            coordinates = 22.7196 to 75.8577,
            capacity = 300,
            facilities = listOf("Water Supply", "Toilets", "Kitchen", "Medical Room"),
            contactNumber = "+91 731 2234568",
            distanceKm = 4.1,
            isGovernmentApproved = true
        ),
        EmergencyShelter(
            id = "shelter_003",
            name = "Sports Complex, Palasia",
            address = "Palasia Square, Indore",
            coordinates = 22.7179 to 75.8678,
            capacity = 800,
            facilities = listOf("Large Space", "Water Supply", "Toilets", "Parking"),
            contactNumber = "+91 731 2234569",
            distanceKm = 5.7,
            isGovernmentApproved = true
        )
    )
}