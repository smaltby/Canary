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
        
    }
    
    static func resume()
    {
        
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
