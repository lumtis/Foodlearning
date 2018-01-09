import React from 'react';
import Particles from 'react-particles-js';

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            model: this.props.model || {greeting: ''}
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

    render() {
        // <h2>{this.state.model.greeting}</h2>

        return (
            <div>
              <Particles
                params={{
                  particles: {
                    number: {
                      value: 15
                    },
                    size: {
                      value: 20,
                      random: false
                    },
                    line_linked: {
                      enable: true
                    },
                    shape: {
                      type: 'image',
                      image: {
                        src: 'https://img15.hostingpics.net/pics/494463burger.png',
                        width: 80,
                        height: 100
                      }
                    },
                    move: {
                      out_mode: 'out',
                      speed: 10,
                      direction: 'top'
                    }
                  }
                }}
                style={{position: 'absolute'}}
              />
              <h1 style={titleStyle}>FoodLearning</h1>
            </div>
        );
    }
}

var titleStyle = {
  position: 'absolute',
  top: '40%',
  left: '43%',
  color: '#E0E0E0',
  fontFamily: 'Verdana'
}

module.exports = Home;
