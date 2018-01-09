import React from 'react';
import Header from './Header.jsx'
import Content from './Content.jsx'

class Container extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <Content {...this.props} />
            </div>
        )
    }
}

module.exports = Container;
