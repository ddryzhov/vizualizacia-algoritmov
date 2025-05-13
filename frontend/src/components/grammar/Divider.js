import React from "react";

/**
 * Divider component placed between panels for resizing.
 * Calls the provided mouse down handler to initiate drag resizing.
 *
 * @param {function} handleMouseDown - Callback for onMouseDown event to start drag.
 */
const Divider = React.memo(({ handleMouseDown }) => (
    <div className="divider" onMouseDown={handleMouseDown} />
));

export default Divider;
