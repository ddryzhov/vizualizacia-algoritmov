import React, { useEffect, useState } from "react";
import "../../assets/styles/blocker/MobileBlocker.css";

const MobileBlocker = () => {
    const [isMobile, setIsMobile] = useState(false);

    useEffect(() => {
        const checkMobile = () => {
            const width = window.innerWidth;
            setIsMobile(width <= 1024);
        };

        checkMobile();
        window.addEventListener("resize", checkMobile);

        return () => {
            window.removeEventListener("resize", checkMobile);
        };
    }, []);

    if (!isMobile) return null;

    return (
        <div className="mobile-blocker">
            <div className="mobile-message">
                ⚠️ This application is not supported on small screens.<br />
                Please use a larger display for full functionality.
            </div>
        </div>
    );
};

export default MobileBlocker;
