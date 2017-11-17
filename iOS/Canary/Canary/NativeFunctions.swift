class NativeFunctions: NSObject
{
    static func playUri(_ uri: String)
    {
        print("Attempting to play " + uri)
        SPTAudioStreamingController.sharedInstance().playSpotifyURI(uri, startingWith: 0, startingWithPosition: 10)
        {
            error in if error != nil
            {
                print("Failed to play: \(error)")
                return
            }
        }
    }
    
    static func pause()
    {
        print("Attempting to pause... ")
        SPTAudioStreamingController.sharedInstance().setIsPlaying(false)
        {
            error in if error != nil
            {
                print("Failed to play: \(error)")
                return
            }
        }
    }
    
    static func resume()
    {
        print("Attempting to resume... ")
        SPTAudioStreamingController.sharedInstance().setIsPlaying(true)
        {
            error in if error != nil
            {
                print("Failed to play: \(error)")
                return
            }
        }
    }
    
    static func next()
    {
        
    }
    
    static func toggleShuffle(_ shuffle: Bool)
    {
        
    }
    
    static func toggleRepeat(_ repeat: Bool)
    {
        
    }
}
