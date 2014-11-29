filetype plugin on
syntax on

" my usual vim customization
set autoindent expandtab smarttab backspace=2
set bg=light
set colorcolumn=80
set cursorline
set hls
set ignorecase
set incsearch
set nobackup
set nocompatible
set relativenumber
set ruler
set scrolloff=3
set softtabstop=2
set tabstop=2
set shiftwidth=2
set tw=78

set scrolloff=3               " scroll offsett, min lines above/below cursor
set scrolljump=5              " jump 5 lines when running out of the screen
set sidescroll=10             " minumum columns to scroll horizontally
set showcmd                   " show command status
set showmatch                 " flashes matching paren when one is typed
set showmode                  " show editing mode in status (-- INSERT --)
set ruler                     " show cursor position

autocmd FileType text set tw=0
autocmd FileType python set ts=4 | set shiftwidth=4 | set expandtab |
  \ set autoindent | set softtabstop=4
autocmd FileType make set noexpandtab | set tabstop=8 | set shiftwidth=8

" kill any trailing whitespace on save
autocmd FileType c,cabal,cpp,haskell,javascript,php,python,readme,text
  \ autocmd BufWritePre <buffer>
  \ :call setline(1,map(getline(1,"$"),'substitute(v:val,"\\s\\+$","","")'))

"
" keyboard mappings (not user-defined function related)
" 

imap jk 
imap  :wa

" Tabs related
cmap -n tabn
cmap -p tabp

" Windows (vim windows) related
map <C-J> <C-W>j
map <C-K> <C-W>k
map <C-L> <C-W>l
map <C-H> <C-W>h

" Add wiki header
:nmap g0 I=A=0f 
:nmap g9 0x$x0f 
:nmap g1 I= A =0f 
:nmap g2 I== A ==0f 
:nmap g3 I=== A ===0f 
:nmap g4 I==== A ====0f 
:nmap g5 I===== A =====0f 

" run current file in python
nmap rpc :!python %
" Copy the content from the current window to another window, close the
" current window

nmap <leader>ca yGPGddgg:q

"
" END keyboard mappings (not user-defined function related)
" 

" not sure what this does
set diffexpr=MyDiff()
function! MyDiff()
  let opt = '-a --binary '
  if &diffopt =~ 'icase' | let opt = opt . '-i ' | endif
  if &diffopt =~ 'iwhite' | let opt = opt . '-b ' | endif
  let arg1 = v:fname_in
  if arg1 =~ ' ' | let arg1 = '"' . arg1 . '"' | endif
  let arg2 = v:fname_new
  if arg2 =~ ' ' | let arg2 = '"' . arg2 . '"' | endif
  let arg3 = v:fname_out
  if arg3 =~ ' ' | let arg3 = '"' . arg3 . '"' | endif
  let eq = ''
  if $VIMRUNTIME =~ ' '
    if &sh =~ '\<cmd'
      let cmd = '""' . $VIMRUNTIME . '\diff"'
      let eq = '"'
    else
      let cmd = substitute($VIMRUNTIME, ' ', '" ', '') . '\diff"'
    endif
  else
    let cmd = $VIMRUNTIME . '\diff'
  endif
  silent execute '!' . cmd . ' ' . opt . arg1 . ' ' . arg2 . ' > ' . arg3 . eq
endfunction

function MoveToPrevTab()
  "there is only one window
  if tabpagenr('$') == 1 && winnr('$') == 1
    return
  endif
  "preparing new window
  let l:tab_nr = tabpagenr('$')
  let l:cur_buf = bufnr('%')
  if tabpagenr() != 1
    close!
    if l:tab_nr == tabpagenr('$')
      tabprev
    endif
    sp
  else
    close!
    exe "0tabnew"
  endif
  "opening current buffer in new window
  exe "b".l:cur_buf
endfunc

function MoveToNextTab()
  "there is only one window
  if tabpagenr('$') == 1 && winnr('$') == 1
    return
  endif
  "preparing new window
  let l:tab_nr = tabpagenr('$')
  let l:cur_buf = bufnr('%')
  if tabpagenr() < tab_nr
    close!
    if l:tab_nr == tabpagenr('$')
      tabnext
    endif
    sp
  else
    close!
    tabnew
  endif
  "opening current buffer in new window
  exe "b".l:cur_buf
endfunc

" hide menu toolbar and scrollbar
function! MinEdit()
    exec('set guioptions-=T go-=m go-=r')
endfunction

" show menu toolbar and scrollbar
function! NormEdit()
    exec('set guioptions+=T go+=m go+=r')
endfunction

command! MinEdit call MinEdit()
command! NormEdit call NormEdit()

MinEdit

set runtimepath=~/.vim/ctrlp,$VIMRUNTIME
" reduce the load of too many entries, so just search file directory or
" current working directory
let g:ctrlp_working_path_mode = 'a'

if exists("g:did_load_filetypes")
  filetype off
  filetype plugin indent off
endif
set runtimepath+=/usr/local/go/misc/vim
filetype plugin indent on
syntax on

autocmd FileType go set noexpandtab ts=4 sts=4 sw=4
autocmd FileType go autocmd BufWritePre <buffer> Fmt
