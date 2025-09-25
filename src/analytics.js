// Google Analytics utility functions
export function trackEvent(eventName, eventCategory = 'Game', eventLabel = '', value = null) {
  if (typeof gtag !== 'undefined') {
    const eventData = {
      event_category: eventCategory,
      event_label: eventLabel,
    };

    if (value !== null) {
      eventData.value = value;
    }

    gtag('event', eventName, eventData);
  }
}

// Game-specific tracking functions
export function trackGameStart(seriesLength, difficulty) {
  trackEvent('game_start', 'Game', `${seriesLength}_games_${difficulty}`);
}

export function trackGameEnd(winner, gameNumber) {
  trackEvent('game_end', 'Game', `winner_${winner}`, gameNumber);
}

export function trackSeriesEnd(seriesWinner, totalGames, playerWins, blockerWins, hunterWins) {
  trackEvent('series_end', 'Series', `winner_${seriesWinner}`, totalGames);
  trackEvent('series_stats', 'Series', 'player_wins', playerWins);
  trackEvent('series_stats', 'Series', 'blocker_wins', blockerWins);
  trackEvent('series_stats', 'Series', 'hunter_wins', hunterWins);
}

export function trackMove(player, cellIndex, moveNumber) {
  trackEvent('move_made', 'Gameplay', `player_${player}_cell_${cellIndex}`, moveNumber);
}

export function trackModalOpen(modalType) {
  trackEvent('modal_open', 'UI', modalType);
}

export function trackButtonClick(buttonName) {
  trackEvent('button_click', 'UI', buttonName);
}

export function trackSeriesAbandoned(gameNumber, totalGames) {
  trackEvent('series_abandoned', 'Game', `game_${gameNumber}_of_${totalGames}`);
}