# File Structure Documentation

## Project Structure Overview

```
Project Root Directory/
├── HTML Files
│   ├── demo.html              # Main page (Dashboard)
│   ├── question-list.html     # Question list page
│   ├── statistics.html        # Statistics page
│   └── today-review.html      # Today's review page
├── styles/                    # CSS Style Files
│   ├── common.css             # Common styles (shared by all pages)
│   ├── dashboard.css          # Dashboard page specific styles
│   ├── question-list.css      # Question list page specific styles
│   ├── statistics.css         # Statistics page specific styles
│   └── today-review.css       # Review page specific styles
├── js/                        # JavaScript Files
│   ├── common.js              # Common JavaScript functionality
│   ├── question-list.js       # Question list page specific functionality
│   ├── statistics.js          # Statistics page specific functionality
│   └── today-review.js        # Review page specific functionality
├── src/                       # Spring Boot Backend Code
│   └── main/java/com/leetcode/tracker/
└── Other Configuration Files
    ├── pom.xml
    ├── run.bat
    └── run.sh
```

## CSS Architecture Design

### 1. common.css - Common Styles
- **CSS Variables Definition**: Design tokens for colors, spacing, shadows, etc.
- **Base Styles**: Reset styles, fonts, layout
- **Common Components**: Buttons, forms, cards, navigation, etc.
- **Responsive Design**: Mobile-first media queries
- **Accessibility**: Focus states, high contrast support

### 2. Page-Specific CSS Files
Each page has an independent CSS file containing only styles specific to that page:

- **dashboard.css**: Dashboard stat cards, demo notices
- **question-list.css**: Form validation, question management interface
- **statistics.css**: Progress bars, statistical charts
- **today-review.css**: Review cards, progress indicators

## JavaScript Architecture Design

### 1. common.js - Common Functionality
- **Notification System**: Unified user feedback mechanism
- **Navigation Handling**: Page navigation and demo mode handling
- **Common Utility Functions**: HTML escaping, loading state management, etc.
- **Event Delegation**: Unified event handling mechanism

### 2. Page-Specific JavaScript Files
Each page has independent functional modules:

- **question-list.js**: Form validation, local storage, question management
- **statistics.js**: Animation effects, interaction enhancements
- **today-review.js**: Progress tracking, review state management

## File Independence Guarantee

### 1. No Duplicate Content
- CSS variables managed uniformly, avoiding duplicate definitions
- Common styles defined once in common.css
- Page-specific styles defined only in corresponding files
- Common JavaScript functionality unified in common.js

### 2. Clear Dependency Relationships
- All HTML files reference `common.css` as the foundation
- Each page only references its own specific CSS file
- JavaScript modularization allows common functionality reuse

### 3. Maintainability Optimization
- Style modifications only need to be made in corresponding files
- Adding new pages only requires creating corresponding CSS and JS files
- Common functionality modifications affect all pages

## Usage Guidelines

### Developing New Pages
1. Create HTML file, reference `common.css`
2. Create corresponding CSS file with only page-specific styles
3. Create corresponding JS file with only page-specific functionality
4. Ensure HTML references all necessary files

### Modifying Styles
- **Global Styles**: Modify `common.css`
- **Page Styles**: Modify corresponding page CSS file
- **Color Theme**: Modify CSS variables in `common.css`

### Adding Features
- **Common Features**: Add to `common.js`
- **Page Features**: Add to corresponding JS file

## Responsive Design

All CSS files adopt mobile-first responsive design:
- **< 768px**: Mobile styles
- **768px - 1199px**: Tablet styles
- **≥ 1200px**: Desktop styles

## Accessibility Features

- Keyboard navigation support
- Focus state styling
- High contrast mode support
- Reduced motion option support

This file structure ensures code modularity, maintainability, and scalability while avoiding duplicate code and style conflicts.

## Key Benefits

### For Developers
- **Easy Maintenance**: Clear separation of concerns
- **Quick Updates**: Modify only what you need
- **Scalable**: Easy to add new pages and features
- **No Conflicts**: Independent file structure prevents style/script conflicts

### For Performance
- **Optimized Loading**: Only load what each page needs
- **Cached Resources**: Common files cached across pages
- **Clean Code**: No redundant styles or scripts

### For Collaboration
- **Clear Ownership**: Each file has a specific purpose
- **Easy Review**: Changes are isolated and trackable
- **Consistent Structure**: New team members can quickly understand the codebase