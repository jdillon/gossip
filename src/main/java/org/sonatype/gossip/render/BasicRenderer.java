/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.gossip.render;

import org.sonatype.gossip.Event;

/**
 * Basic {@link Event} {@link Renderer}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class BasicRenderer
    implements Renderer
{
    private boolean includeName = true;

    private boolean shortName;

    private int nameWidth;

    private boolean includeTimeStamp;

    private boolean includeThread;

    private boolean includeTrace;

    @Override
    public String toString() {
        return "BasicRenderer{" +
            "includeName=" + includeName +
            ", shortName=" + shortName +
            ", nameWidth=" + nameWidth +
            ", includeTimeStamp=" + includeTimeStamp +
            ", includeThread=" + includeThread +
            ", includeTrace=" + includeTrace +
            '}';
    }

    public void setIncludeName(final boolean flag) {
        this.includeName = flag;
    }

    public boolean isIncludeName() {
        return includeName;
    }

    public void setShortName(final boolean flag) {
        this.shortName = flag;
    }

    public boolean isShortName() {
        return shortName;
    }

    public void setNameWidth(final int width) {
        this.nameWidth = width;
    }

    public int getNameWidth() {
        return nameWidth;
    }

    public boolean isIncludeThread() {
        return includeThread;
    }

    public void setIncludeThread(final boolean flag) {
        this.includeThread = flag;
    }

    public boolean isIncludeTimeStamp() {
        return includeTimeStamp;
    }

    public void setIncludeTimeStamp(final boolean flag) {
        this.includeTimeStamp = flag;
    }

    public boolean isIncludeTrace() {
        return includeTrace;
    }

    public void setIncludeTrace(final boolean flag) {
        this.includeTrace = flag;
    }

    public String render(final Event event) {
        assert event != null;

        StringBuilder buff = new StringBuilder();

        if (isIncludeTimeStamp()) {
            renderTimeStamp(event, buff);
            buff.append(" ");
        }

        buff.append("[");
        appendLevel(event, buff);
        buff.append("]");

        switch (event.getLevel()) {
            case INFO:
            case WARN:
                buff.append(" ");
        }

        buff.append(" ");

        if (isIncludeName()) {
            appendLogger(event, buff);
            buff.append(" - ");
        }

        if (isIncludeThread()) {
            buff.append("<");
            renderThreadName(event, buff);
            buff.append("> ");
        }

        appendMessage(event, buff);

        buff.append(NEWLINE);

        if (event.getCause() != null) {
            appendCause(event, buff);
        }

        if (isIncludeTrace()) {
            appendTrace(event, buff);
        }

        return buff.toString();
    }

    protected void renderTimeStamp(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        buff.append(event.getTimeStamp());
    }

    protected void appendLevel(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        buff.append(event.getLevel().name());
    }

    protected void appendLogger(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        String name = event.getName();

        if (isShortName()) {
            int i = name.lastIndexOf(".");

            if (i != -1) {
                name = name.substring(i + 1, name.length());
            }
        }

        int w = getNameWidth();
        if (w > 0) {
            name = String.format("%-" + w + "s", name);
        }

        buff.append(name);
    }

    protected void renderThreadName(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        buff.append(event.getThreadName());
    }

    protected void appendMessage(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        buff.append(event.getMessage());
    }

    protected void appendCause(final Event event, final StringBuilder buff) {
        assert event != null;
        assert buff != null;

        Throwable cause = event.getCause();

        buff.append(cause);
        buff.append(NEWLINE);

        while (cause != null) {
            for (StackTraceElement e : cause.getStackTrace()) {
                buff.append("    at ").append(e.getClassName()).append(".").append(e.getMethodName());
                buff.append(" (").append(getLocation(e)).append(")");
                buff.append(NEWLINE);
            }

            cause = cause.getCause();
            if (cause != null) {
                buff.append("Caused by ").append(cause.getClass().getName()).append(": ");
                buff.append(cause.getMessage());
                buff.append(NEWLINE);
            }
        }
    }

    protected void appendTrace(final Event event, final StringBuilder buff) {
        // TODO:
    }

    protected String getLocation(final StackTraceElement e) {
        assert e != null;

        if (e.isNativeMethod()) {
            return "Native Method";
        }
        else if (e.getFileName() == null) {
            return "Unknown Source";
        }
        else if (e.getLineNumber() >= 0) {
            return String.format("%s:%s", e.getFileName(), e.getLineNumber());
        }
        else {
            return e.getFileName();
        }
    }
}