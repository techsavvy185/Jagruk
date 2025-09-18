# Jagruk - Disaster Preparedness Education App

Jagruk is a comprehensive disaster preparedness education application designed to empower students and communities with life-saving knowledge and practical emergency response skills. The app combines interactive learning modules, real-time emergency alerts, and personalized preparedness planning to create a complete disaster readiness ecosystem.

## Features

### Educational Modules
- **Interactive Learning System**: Comprehensive modules covering earthquake, landslide, flood, cyclone, and first aid preparedness
- **Horizontal Pager Navigation**: Seamless page-by-page learning experience with content and quiz integration
- **Single-Choice Quiz System**: Knowledge assessment with immediate feedback and explanations
- **Progress Tracking**: Visual progress indicators, completion percentages, and achievement badges
- **Gamified Experience**: Badge rewards and streak tracking to encourage consistent engagement

### Emergency Alert System
- **Real-Time Notifications**: Critical emergency alerts with government-level priority
- **Full-Screen Emergency Alerts**: Immersive alert system that bypasses lock screens and Do Not Disturb modes
- **Multi-Modal Alerts**: Combined audio, visual, and haptic feedback for maximum attention
- **Location-Based Targeting**: Geo-targeted alerts relevant to user's specific region
- **Severity Classification**: Color-coded alert levels from low to critical with appropriate response protocols

### Emergency Preparedness Tools
- **Interactive Emergency Kit Checklist**: Comprehensive preparation lists with priority item identification
- **Family Emergency Planning**: Digital tools for creating and maintaining family communication plans
- **Emergency Contact Management**: Quick-access contact system with one-tap emergency calling
- **Shelter Locator**: Government-approved emergency shelter database with real-time distance and facility information
- **Personal Preparedness Dashboard**: Centralized view of readiness status with actionable improvement suggestions

### Technical Implementation
- **Native Android Application**: Built with Kotlin and Jetpack Compose for optimal performance
- **Modern UI/UX Design**: Material 3 design system with accessibility considerations
- **Offline-First Architecture**: Core learning content accessible without internet connectivity
- **Alarm Manager Integration**: Precise timing system for emergency alert scheduling and delivery
- **Notification System**: Advanced notification management with full-screen alert capabilities

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with StateFlow
- **Dependency Injection**: Hilt
- **Navigation**: Navigation Compose
- **Local Data**: Hardcoded data structures (prototype implementation)
- **Permissions**: Advanced Android permission handling for alarms, notifications, and system alerts

## Key Components

### Learning Module System
The educational content is structured into comprehensive modules covering major disaster types. Each module contains:
- Theoretical content pages explaining hazards and safety measures
- Interactive quiz assessments with immediate feedback
- Progress tracking and achievement recognition
- Practical application scenarios and case studies

### Emergency Alert Framework
The emergency notification system provides:
- AlarmManager-based precise scheduling for time-critical alerts
- BroadcastReceiver handling for system-level alert processing
- Full-screen activity overlay for maximum user attention
- Multi-sensory feedback including sound, vibration, and visual cues

### Preparedness Planning Tools
Personal and family emergency preparedness features include:
- Customizable emergency kit checklists with priority categorization
- Family communication plan templates and contact management
- Location-aware shelter identification and navigation assistance
- Document preparation guidelines and storage recommendations

## Usage

### Basic Navigation
1. **Learn Tab**: Access educational modules and track learning progress
2. **Alerts Tab**: View active emergency alerts and safety advisories
3. **My Plan Tab**: Manage personal emergency preparedness and family plans
4. **Hamburger Menu**: Access additional features including emergency SOS testing

### Emergency Alert Testing
1. Navigate to Emergency SOS Test via the hamburger menu
2. Schedule a test alert to experience the full emergency notification system
3. Observe the multi-modal alert delivery including sound, vibration, and full-screen display
4. Test dismissal and response workflows

### Learning Module Progression
1. Select a disaster preparedness module from the Learn tab
2. Navigate through content pages using horizontal swipe gestures
3. Complete embedded quizzes to assess understanding
4. Earn badges and track progress through the gamified system

## Development Considerations

### Prototype Implementation
This version uses hardcoded data structures to demonstrate functionality without requiring backend infrastructure. In a production deployment, these would be replaced with:
- REST API integration for dynamic content delivery
- Real-time emergency alert feeds from government agencies
- User account management and progress synchronization
- Location-based services for personalized content delivery

### Scalability Design
The application architecture supports future enhancements including:
- Multi-language content localization
- Advanced analytics and learning outcome measurement
- Integration with institutional emergency management systems
- Expanded content library with specialized training modules

### Security and Privacy
Current implementation prioritizes user privacy through:
- Local data storage without external transmission
- Minimal permission requests focused on core functionality
- Transparent data usage policies
- Secure handling of personal emergency information

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Educational content adapted from official disaster preparedness guidelines
- Emergency response protocols based on government safety standards
- User experience design inspired by accessibility best practices
- Technical implementation following Android development guidelines

---

This project was developed for Smart India Hackathon 2024 Problem Statement 25008.
