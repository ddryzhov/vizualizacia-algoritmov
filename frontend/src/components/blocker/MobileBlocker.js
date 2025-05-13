import React, { useEffect, useState } from "react";
import "../../assets/styles/blocker/MobileBlocker.css";

/**
 * MobileBlocker component displays a full-screen overlay message
 * when the viewport width is below a defined threshold (1024px).
 * Prevents usage on small screens by blocking interactions.
 */
const MobileBlocker = () => {
    // State flag indicating whether the device is considered mobile
    const [isMobile, setIsMobile] = useState(false);

    useEffect(() => {
        /**
         * Checks window.innerWidth to determine mobile status.
         * Updates `isMobile` if width <= 1024px.
         */
        const checkMobile = () => {
            const width = window.innerWidth;
            setIsMobile(width <= 1024);
        };

        // Initial check and listener registration
        checkMobile();
        window.addEventListener("resize", checkMobile);

        // Clean up listener on unmount
        return () => {
            window.removeEventListener("resize", checkMobile);
        };
    }, []);

    // Render nothing on larger screens
    if (!isMobile) return null;

    // Render blocking overlay on mobile screens
    return (
        <div className="mobile-blocker">
            <div className="mobile-message">
                This application is not supported on small screens.<br />
                Please use a larger display for full functionality.
            </div>
        </div>
    );
};

export default MobileBlocker;
