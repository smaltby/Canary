//
//  ViewController.swift
//  Canary
//
//  Created by Ben Gruber on 1/30/17.
//  Copyright © 2017 Ben Gruber and Sean Maltby. All rights reserved.
//

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
        Hello_Wrapper().hello_cpp_wrapped("World")
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

