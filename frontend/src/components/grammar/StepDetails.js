import React from "react";
import { Typography, Paper } from "@mui/material";

/**
 * Displays the description for the current analysis step.
 */
const StepDetails = React.memo(({ stepDetails }) => (
    <>
        <Typography variant="h6" className="step-details-title">
            Step Details
        </Typography>
        <Paper className="step-details">
            <Typography style={{ whiteSpace: "pre-line" }}>
                {stepDetails || "No details available"}
            </Typography>
        </Paper>
    </>
));

export default StepDetails;
