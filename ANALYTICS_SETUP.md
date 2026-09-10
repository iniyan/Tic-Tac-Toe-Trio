# Google Analytics Setup

## Configuration

To enable Google Analytics tracking, you need to replace the placeholder `GA_MEASUREMENT_ID` in `index.html` with your actual Google Analytics Measurement ID.

### Steps:

1. Create a Google Analytics 4 property at [Google Analytics](https://analytics.google.com/)
2. Get your Measurement ID (format: G-XXXXXXXXXX)
3. Replace both instances of `GA_MEASUREMENT_ID` in `index.html` with your actual ID:
   ```html
   <script async src="https://www.googletagmanager.com/gtag/js?id=YOUR_ACTUAL_ID"></script>
   <script>
     window.dataLayer = window.dataLayer || [];
     function gtag(){dataLayer.push(arguments);}
     gtag('js', new Date());
     gtag('config', 'YOUR_ACTUAL_ID');
   </script>
   ```

## Tracked Events

The game tracks the following events:

### Game Events
- **game_start**: When a new series begins (with series length and difficulty)
- **game_end**: When individual games end (with winner and game number)
- **series_end**: When a series completes (with final statistics)
- **series_abandoned**: When a player abandons a series mid-way

### Gameplay Events
- **move_made**: Each move made by players (with player, position, and move number)

### UI Events
- **button_click**: All button interactions
- **modal_open**: When help/rules modal opens

This provides comprehensive analytics to understand user engagement and gameplay patterns.