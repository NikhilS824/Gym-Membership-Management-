export function playExpiryAlert() {
  try {
    const audio = new window.AudioContext()
    const play = () => {
      const oscillator = audio.createOscillator()
      const gain = audio.createGain()
      oscillator.type = 'sine'
      oscillator.frequency.value = 740
      gain.gain.setValueAtTime(0.08, audio.currentTime)
      gain.gain.exponentialRampToValueAtTime(0.001, audio.currentTime + 0.65)
      oscillator.connect(gain)
      gain.connect(audio.destination)
      oscillator.start()
      oscillator.stop(audio.currentTime + 0.65)
      oscillator.onended = () => audio.close()
    }
    if (audio.state === 'suspended') {
      audio.resume().then(play).catch((error) => {
        console.warn('The expiry alert sound could not be played.', error)
        audio.close()
      })
    } else play()
  } catch (error) {
    console.warn('The expiry alert sound could not be played.', error)
  }
}
