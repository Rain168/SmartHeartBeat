/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.mina.core.filterchain;

import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import java.util.List;

/**
 * A container of {@link IoFilter}s that forwards {@link org.apache.mina.core.service.IoHandler} events
 * to the consisting filters and terminal {@link org.apache.mina.core.service.IoHandler} sequentially.
 * Every {@link org.apache.mina.core.session.IoSession} has its own {@link IoFilterChain} (1-to-1 relationship).
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public interface IoFilterChain {
    /**
     * Returns the parent {@link org.apache.mina.core.session.IoSession} of this chain.
     *
     * @return {@link org.apache.mina.core.session.IoSession}
     */
    IoSession getSession();

    /**
     * Returns the {@link org.apache.mina.core.filterchain.IoFilterChain.Entry} with the specified <tt>name</tt> in this chain.
     *
     * @param name The filter's name we are looking for
     * @return <tt>null</tt> if there's no such name in this chain
     */
    Entry getEntry(String name);

    /**
     * Returns the {@link org.apache.mina.core.filterchain.IoFilterChain.Entry} with the specified <tt>filter</tt> in this chain.
     *
     * @param filter  The Filter we are looking for
     * @return <tt>null</tt> if there's no such filter in this chain
     */
    Entry getEntry(IoFilter filter);

    /**
     * Returns the {@link org.apache.mina.core.filterchain.IoFilterChain.Entry} with the specified <tt>filterType</tt>
     * in this chain. If there's more than one filter with the specified
     * type, the first match will be chosen.
     *
     * @param filterType The filter class we are looking for
     * @return <tt>null</tt> if there's no such name in this chain
     */
    Entry getEntry(Class<? extends IoFilter> filterType);

    /**
     * Returns the {@link IoFilter} with the specified <tt>name</tt> in this chain.
     *
     * @param name the filter's name
     * @return <tt>null</tt> if there's no such name in this chain
     */
    IoFilter get(String name);

    /**
     * Returns the {@link IoFilter} with the specified <tt>filterType</tt>
     * in this chain. If there's more than one filter with the specified
     * type, the first match will be chosen.
     *
     * @param filterType The filter class
     * @return <tt>null</tt> if there's no such name in this chain
     */
    IoFilter get(Class<? extends IoFilter> filterType);

    /**
     * Returns the {@link org.apache.mina.core.filterchain.IoFilter.NextFilter} of the {@link IoFilter} with the
     * specified <tt>name</tt> in this chain.
     *
     * @param name The filter's name we want the next filter
     * @return <tt>null</tt> if there's no such name in this chain
     */
    NextFilter getNextFilter(String name);

    /**
     * Returns the {@link org.apache.mina.core.filterchain.IoFilter.NextFilter} of the specified {@link IoFilter}
     * in this chain.
     *
     * @param filter The filter for which we want the next filter
     * @return <tt>null</tt> if there's no such name in this chain
     */
    NextFilter getNextFilter(IoFilter filter);

    /**
     * Returns the {@link org.apache.mina.core.filterchain.IoFilter.NextFilter} of the specified <tt>filterType</tt>
     * in this chain.  If there's more than one filter with the specified
     * type, the first match will be chosen.
     *
     * @param filterType The Filter class for which we want the next filter
     * @return <tt>null</tt> if there's no such name in this chain
     */
    NextFilter getNextFilter(Class<? extends IoFilter> filterType);

    /**
     * @return The list of all {@link org.apache.mina.core.filterchain.IoFilterChain.Entry}s this chain contains.
     */
    List<Entry> getAll();

    /**
     * @return The reversed list of all {@link org.apache.mina.core.filterchain.IoFilterChain.Entry}s this chain contains.
     */
    List<Entry> getAllReversed();

    /**
     * @param name The filter's name we are looking for
     *
     * @return <tt>true</tt> if this chain contains an {@link IoFilter} with the
     * specified <tt>name</tt>.
     */
    boolean contains(String name);

    /**
     * @param filter The filter we are looking for
     *
     * @return <tt>true</tt> if this chain contains the specified <tt>filter</tt>.
     */
    boolean contains(IoFilter filter);

    /**
     * @param  filterType The filter's class we are looking for
     *
     * @return <tt>true</tt> if this chain contains an {@link IoFilter} of the
     * specified <tt>filterType</tt>.
     */
    boolean contains(Class<? extends IoFilter> filterType);

    /**
     * Adds the specified filter with the specified name at the beginning of this chain.
     *
     * @param name The filter's name
     * @param filter The filter to add
     */
    void addFirst(String name, IoFilter filter);

    /**
     * Adds the specified filter with the specified name at the end of this chain.
     *
     * @param name The filter's name
     * @param filter The filter to add
     */
    void addLast(String name, IoFilter filter);

    /**
     * Adds the specified filter with the specified name just before the filter whose name is
     * <code>baseName</code> in this chain.
     *
     * @param baseName The targeted Filter's name
     * @param name The filter's name
     * @param filter The filter to add
     */
    void addBefore(String baseName, String name, IoFilter filter);

    /**
     * Adds the specified filter with the specified name just after the filter whose name is
     * <code>baseName</code> in this chain.
     *
     * @param baseName The targeted Filter's name
     * @param name The filter's name
     * @param filter The filter to add
     */
    void addAfter(String baseName, String name, IoFilter filter);

    /**
     * Replace the filter with the specified name with the specified new
     * filter.
     *
     * @param name The name of the filter we want to replace
     * @param newFilter The new filter
     * @return the old filter
     */
    IoFilter replace(String name, IoFilter newFilter);

    /**
     * Replace the filter with the specified name with the specified new
     * filter.
     *
     * @param oldFilter The filter we want to replace
     * @param newFilter The new filter
     */
    void replace(IoFilter oldFilter, IoFilter newFilter);

    /**
     * Replace the filter of the specified type with the specified new
     * filter.  If there's more than one filter with the specified type,
     * the first match will be replaced.
     *
     * @param oldFilterType The filter class we want to replace
     * @param newFilter The new filter
     */
    IoFilter replace(Class<? extends IoFilter> oldFilterType, IoFilter newFilter);

    /**
     * Removes the filter with the specified name from this chain.
     *
     * @param name The name of the filter to remove
     * @return The removed filter
     */
    IoFilter remove(String name);

    /**
     * Replace the filter with the specified name with the specified new filter.
     *
     * @param filter
     *            The filter to remove
     */
    void remove(IoFilter filter);

    /**
     * Replace the filter of the specified type with the specified new filter.
     * If there's more than one filter with the specified type, the first match
     * will be replaced.
     *
     * @param filterType
     *            The filter class to remove
     * @return The removed filter
     */
    IoFilter remove(Class<? extends IoFilter> filterType);

    /**
     * Removes all filters added to this chain.
     */
    void clear() throws Exception;

    /**
     * Fires a {@link org.apache.mina.core.service.IoHandler#sessionCreated(org.apache.mina.core.session.IoSession)} event. Most users don't need to
     * call this method at all. Please use this method only when you implement a new transport
     * or fire a virtual event.
     */
    public void fireSessionCreated();

    /**
     * Fires a {@link org.apache.mina.core.service.IoHandler#sessionOpened(org.apache.mina.core.session.IoSession)} event. Most users don't need to call
     * this method at all. Please use this method only when you implement a new transport or
     * fire a virtual event.
     */
    public void fireSessionOpened();

    /**
     * Fires a {@link org.apache.mina.core.service.IoHandler#sessionClosed(org.apache.mina.core.session.IoSession)} event. Most users don't need to call
     * this method at all. Please use this method only when you implement a new transport or
     * fire a virtual event.
     */
    public void fireSessionClosed();

    /**
     * Fires a {@link org.apache.mina.core.service.IoHandler#sessionIdle(org.apache.mina.core.session.IoSession, org.apache.mina.core.session.IdleStatus)} event. Most users don't
     * need to call this method at all. Please use this method only when you implement a new
     * transport or fire a virtual event.
     *
     * @param status The current status to propagate
     */
    public void fireSessionIdle(IdleStatus status);

    /**
     * Fires a {@link org.apache.mina.core.service.IoHandler#messageReceived(org.apache.mina.core.session.IoSession, Object)} event. Most
     * users don't need to call this method at all. Please use this method only
     * when you implement a new transport or fire a virtual event.
     *
     * @param message
     *            The received message
     */
    public void fireMessageReceived(Object message);

    /**
     * Fires a {@link org.apache.mina.core.service.IoHandler#messageSent(org.apache.mina.core.session.IoSession, Object)} event. Most
     * users don't need to call this method at all. Please use this method only
     * when you implement a new transport or fire a virtual event.
     *
     * @param request
     *            The sent request
     */
    public void fireMessageSent(WriteRequest request);

    /**
     * Fires a {@link org.apache.mina.core.service.IoHandler#exceptionCaught(org.apache.mina.core.session.IoSession, Throwable)} event. Most users don't
     * need to call this method at all. Please use this method only when you implement a new
     * transport or fire a virtual event.
     *
     * @param cause The exception cause
     */
    public void fireExceptionCaught(Throwable cause);

    /**
     * Fires a {@link org.apache.mina.core.service.IoHandler#inputClosed(org.apache.mina.core.session.IoSession)} event. Most users don't
     * need to call this method at all. Please use this method only when you
     * implement a new transport or fire a virtual event.
     */
    public void fireInputClosed();

    /**
     * Fires a {@link org.apache.mina.core.session.IoSession#write(Object)} event. Most users don't need to
     * call this method at all. Please use this method only when you implement a
     * new transport or fire a virtual event.
     *
     * @param writeRequest
     *            The message to write
     */
    public void fireFilterWrite(WriteRequest writeRequest);

    /**
     * Fires a {@link org.apache.mina.core.session.IoSession#close()} event. Most users don't need to call this method at
     * all. Please use this method only when you implement a new transport or fire a virtual
     * event.
     */
    public void fireFilterClose();

    /**
     * Represents a name-filter pair that an {@link IoFilterChain} contains.
     *
     * @author <a href="http://mina.apache.org">Apache MINA Project</a>
     */
    public interface Entry {
        /**
         * Returns the name of the filter.
         */
        String getName();

        /**
         * Returns the filter.
         */
        IoFilter getFilter();

        /**
         * @return The {@link org.apache.mina.core.filterchain.IoFilter.NextFilter} of the filter.
         */
        NextFilter getNextFilter();

        /**
         * Adds the specified filter with the specified name just before this entry.
         */
        void addBefore(String name, IoFilter filter);

        /**
         * Adds the specified filter with the specified name just after this entry.
         */
        void addAfter(String name, IoFilter filter);

        /**
         * Replace the filter of this entry with the specified new filter.
         */
        void replace(IoFilter newFilter);

        /**
         * Removes this entry from the chain it belongs to.
         */
        void remove();
    }
}
