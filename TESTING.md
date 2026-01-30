# Atlas Android - Testing Guide

Quick guide for testing the completed Phase 1 + Phase 2 features.

---

## Prerequisites

### Required
- Android device or emulator (Android 8.0+ / API 26+)
- OpenClaw Gateway running locally or accessible via network
- Microphone access for voice features

### Setup
1. Ensure OpenClaw Gateway is running:
   ```bash
   openclaw gateway status
   # Should show: running
   ```

2. Note your gateway URL (default: `http://localhost:18789`)
3. If testing on a physical device, update the gateway URL to your machine's IP

### Build Configuration
Edit `app/build.gradle.kts` if needed:
```kotlin
buildConfigField("String", "GATEWAY_URL", "\"http://YOUR_IP:18789\"")
```

---

## Testing Phase 1: Text Chat

### Test 1: Basic Message Flow
1. Launch the app
2. Type "Hello Atlas" in the text input field
3. Press "Send"

**Expected:**
- ✅ Your message appears on the right (blue bubble)
- ✅ Avatar transitions to THINKING state (purple rotating particles)
- ✅ Atlas's response appears on the left (grey bubble)
- ✅ Avatar returns to IDLE state (gentle breathing pulse)
- ✅ Status bar updates with token count

### Test 2: Status Bar
1. Send a few messages
2. Observe the status bar at the top

**Expected:**
- ✅ Shows current model (e.g., "claude-sonnet-4-5")
- ✅ Token count increases with each exchange
- ✅ Updates automatically after each response

### Test 3: Message History
1. Send multiple messages
2. Scroll through the chat

**Expected:**
- ✅ Messages stay in order
- ✅ Auto-scrolls to bottom on new messages
- ✅ User messages on right, Atlas on left
- ✅ Clear visual distinction

### Test 4: Error Handling
1. Stop the OpenClaw Gateway
2. Try sending a message

**Expected:**
- ✅ Avatar shows ERROR state (red, shaking)
- ✅ Snackbar displays error message
- ✅ App doesn't crash

---

## Testing Phase 2: Voice Conversation

### Test 5: Permission Flow
1. Fresh install or clear app data
2. Press the microphone button

**Expected:**
- ✅ Permission dialog appears
- ✅ If granted: listening starts
- ✅ If denied: error snackbar appears

### Test 6: Voice Input
1. Press the microphone button (bottom left)
2. Speak clearly: "What's the weather like?"
3. Stop speaking and wait

**Expected:**
- ✅ Mic button turns red with stop icon
- ✅ Avatar shows LISTENING state (green expanding rings)
- ✅ Speech recognition captures your words
- ✅ Avatar transitions to THINKING
- ✅ Your transcribed message appears in chat

### Test 7: Voice Output (TTS)
1. Continue from Test 6 (or send any voice message)
2. Wait for Atlas's response

**Expected:**
- ✅ Avatar transitions to SPEAKING state (cyan pulsing)
- ✅ Audio plays through device speaker
- ✅ Avatar pulses in sync with speech (ideally)
- ✅ When audio ends, avatar returns to IDLE
- ✅ Response text also appears in chat

### Test 8: Voice Conversation Loop
1. Press mic button
2. Say "Tell me a joke"
3. Listen to response
4. Press mic again
5. Say "Tell me another one"

**Expected:**
- ✅ Smooth state transitions (Idle → Listening → Thinking → Speaking → Idle)
- ✅ No UI lag or stuttering
- ✅ Audio plays clearly
- ✅ Can interrupt if needed (press stop button)

### Test 9: Stop Voice Input
1. Press mic button
2. Immediately press the stop button (red)

**Expected:**
- ✅ Listening stops
- ✅ Avatar returns to IDLE
- ✅ No crash or error

---

## Testing Avatar Animations

### Test 10: All Avatar States
Trigger each state and observe the animation:

1. **IDLE (default)**
   - Gentle scale animation (0.95x → 1.05x)
   - Blue color
   - Breathing effect

2. **LISTENING (press mic)**
   - Expanding concentric rings
   - Green color
   - Pulsing center

3. **THINKING (send message)**
   - Rotating orbital particles (6 dots)
   - Purple color
   - Spinning motion

4. **SPEAKING (voice response)**
   - Fast pulsing
   - Cyan color
   - Frequency-based scale

5. **ERROR (disconnect gateway, send message)**
   - Shake animation
   - Red/orange color
   - Rapid horizontal oscillation

**Expected:**
- ✅ All animations smooth (60fps)
- ✅ No lag or frame drops
- ✅ Colors match design spec
- ✅ Transitions are instant

---

## Testing Edge Cases

### Test 11: Network Timeout
1. Set gateway to incorrect URL
2. Try sending a message

**Expected:**
- ✅ Shows error after timeout (~30s)
- ✅ Avatar goes to ERROR state
- ✅ Error message displayed

### Test 12: Empty Message
1. Leave text input blank
2. Try to press "Send"

**Expected:**
- ✅ Send button is disabled (greyed out)
- ✅ Nothing happens

### Test 13: Voice No Speech
1. Press mic button
2. Don't say anything (wait ~5 seconds)

**Expected:**
- ✅ Error: "No speech detected"
- ✅ Avatar returns to IDLE
- ✅ No crash

### Test 14: Background/Foreground
1. Start a voice conversation
2. Press home button (background the app)
3. Return to app

**Expected:**
- ✅ State is preserved
- ✅ No crash
- ⚠️ Voice may stop (expected Android behavior)

### Test 15: Dark/Light Theme
1. Change device theme (Settings → Display → Dark mode)
2. Relaunch app

**Expected:**
- ✅ UI adapts to theme
- ✅ Colors appropriate for theme
- ✅ Text remains readable
- ✅ Avatar colors unchanged (by design)

---

## Performance Testing

### Test 16: Long Conversation
1. Send 20+ messages
2. Observe performance

**Expected:**
- ✅ No memory leaks
- ✅ Smooth scrolling
- ✅ No slowdown
- ✅ Status bar updates correctly

### Test 17: Rapid Input
1. Send 5 messages quickly (before responses arrive)

**Expected:**
- ✅ App queues or handles gracefully
- ✅ No crashes
- ⚠️ May show errors if gateway overwhelmed

---

## Known Issues (Expected)

### Gateway URL Hardcoded
- Currently set in BuildConfig
- Needs manual change for remote gateway
- **Workaround:** Edit `build.gradle.kts` before building

### TTS May Fail
- If OpenClaw TTS endpoint not configured
- **Workaround:** App falls back to text-only (no crash)

### Voice Recognition Quirks
- May not work in noisy environments
- Some accents less accurate
- **Expected:** Android SpeechRecognizer limitation

### No Conversation Persistence
- Chat history clears on app restart
- **Expected:** Phase 3 feature

---

## Reporting Issues

If you find a bug during testing, note:
1. Device/emulator details (Android version, model)
2. Steps to reproduce
3. Expected vs. actual behavior
4. Logcat output (if applicable)

---

## Success Criteria

All tests should pass for Phase 1+2 to be considered complete:

**Phase 1:**
- ✅ Text messages send and receive
- ✅ Status bar shows model + tokens
- ✅ UI is responsive and polished

**Phase 2:**
- ✅ Voice input works (mic button)
- ✅ Voice output plays (TTS audio)
- ✅ Avatar animates for all 5 states
- ✅ Full voice conversation loop functional

---

**Last Updated:** January 30, 2026  
**Tested By:** (Awaiting first test run)  
**Status:** Ready for QA
