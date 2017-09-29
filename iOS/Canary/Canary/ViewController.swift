import UIKit

class ViewController: UIViewController, SPTAudioStreamingDelegate, SPTAudioStreamingPlaybackDelegate {
    @IBOutlet weak var commandTextField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        self.handleNewSession()
        print("session: \(SPTAuth.defaultInstance().session.accessToken!)")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func sendCommand(_ sender: UIButton) {
        print(commandTextField.text!)
        let result:String = NativeBridge().parse_wrapped(commandTextField.text, with_token: SPTAuth.defaultInstance().session.accessToken)
        print(result)
    }
    
    func handleNewSession()
    {
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
    }
}

