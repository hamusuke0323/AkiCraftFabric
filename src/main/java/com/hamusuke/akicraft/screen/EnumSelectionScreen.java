package com.hamusuke.akicraft.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class EnumSelectionScreen<T extends Enum<T>> extends Screen implements RelatedToAkiScreen {
    private final Consumer<Enum<T>> consumer;
    private final Class<T> enumClass;
    private List list;
    @Nullable
    private final Enum<T> selected;

    public EnumSelectionScreen(Consumer<Enum<T>> consumer, Class<T> enumClass, @Nullable Enum<T> selected) {
        super(NarratorManager.EMPTY);
        this.consumer = consumer;
        this.enumClass = enumClass;
        this.selected = selected;
    }

    @Override
    protected void init() {
        this.list = new List();

        this.list.children().forEach(entry -> {
            if (entry.enumeration.equals(this.selected)) {
                this.list.setSelected(entry);
            }
        });

        this.addSelectableChild(this.list);
        this.addDrawableChild(new ButtonWidget(this.width / 4, this.height - 20, this.width / 2, 20, ScreenTexts.DONE, p_93751_ -> {
            List.Entry e = this.list.getSelectedOrNull();
            this.consumer.accept(e == null ? null : e.enumeration);
        }));
    }

    @Override
    public void render(MatrixStack p_96562_, int p_96563_, int p_96564_, float p_96565_) {
        this.list.render(p_96562_, p_96563_, p_96564_, p_96565_);
        super.render(p_96562_, p_96563_, p_96564_, p_96565_);
    }

    final class List extends EntryListWidget<List.Entry> {
        public List() {
            super(EnumSelectionScreen.this.client, EnumSelectionScreen.this.width, EnumSelectionScreen.this.height, 20, EnumSelectionScreen.this.height - 20, 20);
            for (Enum<T> e : EnumSelectionScreen.this.enumClass.getEnumConstants()) {
                this.addEntry(new Entry(e));
            }
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {
        }

        final class Entry extends EntryListWidget.Entry<Entry> {
            private final Enum<T> enumeration;

            private Entry(Enum<T> enumeration) {
                this.enumeration = enumeration;
            }

            @Override
            public boolean mouseClicked(double p_94737_, double p_94738_, int p_94739_) {
                if (p_94739_ == 0) {
                    List.this.setSelected(this);
                    return true;
                }

                return false;
            }

            @Override
            public void render(MatrixStack p_93523_, int p_93524_, int p_93525_, int p_93526_, int p_93527_, int p_93528_, int p_93529_, int p_93530_, boolean p_93531_, float p_93532_) {
                EnumSelectionScreen.drawCenteredText(p_93523_, EnumSelectionScreen.this.textRenderer, this.enumeration.name(), p_93526_ + p_93527_ / 2, p_93525_ + 5, 16777215);
            }
        }
    }
}