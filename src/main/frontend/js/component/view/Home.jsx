import React from 'react';
import Particles from 'react-particles-js';

class Home extends React.Component {
    constructor(props) {
        super(props);

        this.addInput = this.addInput.bind(this)

        this.state = {
            model: this.props.model || {greeting: ''},
            inputList: []
        }
    }

    loadModelFromServer = () => {
        let url = '/data/home';
        let header = new Headers({"Content-type": "application/json"});
        let init = {
            method: 'GET',
            header: header,
            cache: 'no-cache'
        };
        let request = new Request(url, init);
        fetch(request).then((response) => {
            if (response.ok) {
                return response.json();
            }
            throw new Error(`Network response was not ok: status=${response.status}`);
        }).then((result) => {
            this.setState({model: result});
        }).catch((error) => {
            console.error(`Cannot fetch data from the server: url=${url}, error=${error.message}`)
        });
    }

    componentDidMount = () => {
        this.loadModelFromServer();
    }

    addInput(e) {
      e.preventDefault();

      var inputListTmp = this.state.inputList
      inputListTmp.push(<input style={inputStyle}/>)
      this.setState({inputList: inputListTmp})
    }

    render() {
        // <h2>{this.state.model.greeting}</h2>

        return (
            <div>
            <Particles
                params={{
                  particles: {
                    number: {
                      value: 25
                    },
                    size: {
                      value: 8,
                      random: false
                    },
                    line_linked: {
                      shadow: {
              					enable: true,
              					color: "#3CA9D1",
              					blur: 5
              				}
                    //  enable: false
                    },
                    shape: {
                      type: 'image',
                      image: {
                        src: 'https://img15.hostingpics.net/pics/494463burger.png',
                        width: 40,
                        height: 50
                      }
                    },
                    move: {
                      out_mode: 'out',
                      speed: 5,
                      direction: 'top'
                    }
                  }
                }}
                style={{position: 'absolute'}}
              />
              <h1 style={titleStyle}>FoodLearning</h1>
              <div style={ingredientsStyle}>
                <div style={buttonStyle}>
                  <a onClick={this.addInput}><button>+++</button></a>
                  <a><button>Send</button></a>
                </div>
                {this.state.inputList}
              </div>
            </div>
        );
    }
}

var titleStyle = {
  position: 'absolute',
  top: '20%',
  left: '43%',
  color: '#E0E0E0',
  fontFamily: 'Verdana'
}

var ingredientsStyle = {
  position: 'absolute',
  top: '30%',
  left: '45%',
  width: '200px',
  color: '#E0E0E0',
  fontFamily: 'Verdana'
}

var inputStyle = {
  marginBottom: '10px',
  width:'180px'
}

var buttonStyle = {
  textAlign: 'center',
  margin: '10px'
}


module.exports = Home;
