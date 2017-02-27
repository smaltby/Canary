import UIKit

let appDelegate = UIApplication.shared.delegate as! AppDelegate

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, SPTAudioStreamingDelegate, SPTAudioStreamingPlaybackDelegate {

    var window: UIWindow?
	var session: SPTSession?
	var player: SPTAudioStreamingController?
	let kClientId = "3e189d315fa64c97abdeaf9f855815e3"
	let kCallbackURL = "canary://callback"
	let kSessionUserDefaultsKey = "SpotifySession"

	func delay(_ delay:Double, closure:@escaping ()->()) {
		DispatchQueue.main.asyncAfter(
			deadline: DispatchTime.now() + Double(Int64(delay * Double(NSEC_PER_SEC))) / Double(NSEC_PER_SEC), execute: closure)
	}

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
		print("Hello World in Swift")
		// Set variables necessary for authentication
		SPTAuth.defaultInstance().clientID = kClientId
		SPTAuth.defaultInstance().redirectURL = URL(string:kCallbackURL)
		SPTAuth.defaultInstance().requestedScopes = [SPTAuthStreamingScope]
		SPTAuth.defaultInstance().sessionUserDefaultsKey = kSessionUserDefaultsKey
		
		// Start AudioStreamingController
		do
		{
			try SPTAudioStreamingController.sharedInstance().start(withClientId: SPTAuth.defaultInstance().clientID, audioController: nil, allowCaching: true)
			SPTAudioStreamingController.sharedInstance().delegate = self
			SPTAudioStreamingController.sharedInstance().playbackDelegate = self
			SPTAudioStreamingController.sharedInstance().diskCache = SPTDiskCache()
			SPTAudioStreamingController.sharedInstance().login(withAccessToken: SPTAuth.defaultInstance().session.accessToken!)
		} catch let error
		{
			print("Failed to start: \(error)")
		}
        return true
    }
	
	func application(_ application: UIApplication, open url: URL, sourceApplication: String?, annotation: Any) -> Bool {
		// Ask SPTAuth if the URL given is a Spotify authentication callback
		print("The URL: \(url)")
		if SPTAuth.defaultInstance().canHandle(url) {
			SPTAuth.defaultInstance().handleAuthCallback(withTriggeredAuthURL: url) { error, session in
				// This is the callback that'll be triggered when auth is completed (or fails).
				if error != nil {
					print("*** Auth error: \(error)")
					return
				}
				else {
					SPTAuth.defaultInstance().session = session
				}
				NotificationCenter.default.post(name: NSNotification.Name.init(rawValue: "sessionUpdated"), object: self)
			}
		}
		return false
	}

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }


}

