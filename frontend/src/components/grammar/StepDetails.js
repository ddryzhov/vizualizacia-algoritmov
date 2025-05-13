import React from "react";
import { Typography, Paper } from "@mui/material";
import { useTranslation } from "react-i18next";

/**
 * Component to display a detailed description of the current analysis step.
 * Renders a title and the step description, preserving line breaks.
 *
 * @param {string} stepDetails - Description text for the current step.
 */
const StepDetails = React.memo(({ stepDetails }) => {
    const { t } = useTranslation();

    return (
        <>
            {/* Section title for step details */}
            <Typography variant="h6" className="step-details-title">
                {t("Step Details")}
            </Typography>
            {/* Container for step description */}
            <Paper className="step-details">
                <Typography style={{ whiteSpace: "pre-line" }}>
                    {stepDetails || t("No details available")}
                </Typography>
            </Paper>
        </>
    );
});

export default StepDetails;
