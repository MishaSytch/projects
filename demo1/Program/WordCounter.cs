using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace Program
{
    public class WordCounter
    {
        private string _path;
        private string _name;
        private List<Info> _infos = new List<Info>();

        public class Info
        {
            public string Word { get;}
            public long Frequency { get; set; }

            public Info(string word)
            {
                this.Frequency = 1;
                this.Word = word;
            }

            public void AddToFrequency()
            {
                Frequency++;
            }
        }

        public WordCounter(string path, string name)
        {
            this._path = path;
            this._name = name;

        }

        public void Count()
        {
            try
            {
                using (StreamReader streamReader = new StreamReader($"{_path}/{_name}"))
                {
                    string line;
                    while ((line = (streamReader.ReadLine())) != null)
                    {
                        string[] strings = line.Trim().Split(' ');
                        for (int i = 0; i < strings.Length; i++)
                        {
                            string tmp = strings[i];
                            
                            string variable = "";
                            foreach (var letter in tmp)
                                if (char.IsLetter(letter) || letter.Equals("'")) //Регулярку вставить бы
                                    variable += letter;
                            
                            bool exist = false;
                            foreach (var info in _infos)
                            {
                                if (info.Word.Equals(variable.ToLower()))
                                {
                                    info.AddToFrequency();
                                    exist = true;
                                    break;
                                }
                            }
                            if (!exist && variable.Length > 0)
                            {
                                _infos.Add(new Info(variable.ToLower()));
                            }
                        }
                    }
                }
                Save();
                Console.WriteLine("Файл Count.txt с информацией о тексте создан без ошибок");
            }
            catch (Exception e)
            {
                ConsoleColor def = Console.ForegroundColor;
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine($"Ошибка выполнения\n{e.Message}");
                Console.ForegroundColor = def;
            }
        }

        private void Save()
        {
            string text = DictToString();
            using (StreamWriter streamWriter = File.CreateText($"{_path}/count.txt"))
            {
                streamWriter.WriteLine(text);
            }
            
        }

        private string DictToString()
        {
            var infos = _infos.OrderByDescending(s => s.Frequency).ThenBy(s => s.Word);
            string str = "";
            foreach (var VARIABLE in infos) str += String.Format("{0,-24} {1}\n", VARIABLE.Word, VARIABLE.Frequency);
            return str;
        }
    }
}