import UIKit

class ViewController: UIViewController {
    @IBOutlet weak var commandTextField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
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
}

