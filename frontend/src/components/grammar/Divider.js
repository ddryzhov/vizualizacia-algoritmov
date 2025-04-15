import React from "react";

/**
 * Vertical divider between left and right panels.
 * Supports mouse drag to resize layout.
 */
const Divider = React.memo(({ handleMouseDown }) => (
    <div className="divider" onMouseDown={handleMouseDown} />
));

export default Divider;
