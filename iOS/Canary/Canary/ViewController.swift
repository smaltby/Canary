import UIKit

class ViewController: UIViewController {
    
    //MARK: Properties
    
    @IBOutlet weak var searchBox: UITextField!
    
    @IBOutlet var albumOutput: UILabel!
    
    @IBOutlet weak var searchButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        print("Hello World in Swift")
        CommandParser_Wrapper().parse_wrapped("play gates by speak", with_token: "token")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    //MARK: Actions
    
    @IBAction func searchButtonPressed(_ sender: AnyObject) {
        albumOutput.text = searchBox.text
    }
    
}

