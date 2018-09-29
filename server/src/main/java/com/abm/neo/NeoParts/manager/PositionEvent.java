package com.abm.pos.ABMPos.manager;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;


class PositionEvent implements PdfPCellEvent {
    protected Phrase content;
    protected float wPct;
    protected float hPct;
    protected int alignment;

    public PositionEvent(Phrase content, float wPct, float hPct, int alignment) {
        this.content = content;
        this.wPct = wPct;
        this.hPct = hPct;
        this.alignment = alignment;
    }

    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[PdfPTable.TEXTCANVAS];
        float x = position.getLeft() + wPct * position.getWidth();
        float y = position.getBottom() + hPct * (position.getHeight() - content.getLeading());
        ColumnText.showTextAligned(canvas, alignment, content, x, y, 0);
    }

}
