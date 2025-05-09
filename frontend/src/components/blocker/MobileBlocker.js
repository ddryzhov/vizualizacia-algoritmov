import React, { useEffect, useState } from "react";
import "../../assets/styles/blocker/MobileBlocker.css";

const MobileBlocker = () => {
    const [isMobile, setIsMobile] = useState(false);

    useEffect(() => {
        // Function to check screen width and update isMobile state
        const checkMobile = () => {
            const width = window.innerWidth;
            setIsMobile(width <= 1024); // Treat width <= 1024px as mobile
        };

        checkMobile();
        window.addEventListener("resize", checkMobile);

        return () => {
            window.removeEventListener("resize", checkMobile);
        };
    }, []);

    // If not mobile, render nothing
    if (!isMobile) return null;

    // If mobile, show blocking message
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
